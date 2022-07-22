package xue.xiang.yi.notice.deal;

import cc.eslink.paycenter.gateway.domain.BizChannelTradeOrderError;
import cc.eslink.paycenter.gateway.service.inner.BizChannelTradeOrderErrorInnerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @DATE: 2020/12/1
 * @AUTHOR: 薛向毅
 * desc:
 **/
public abstract class BaseDeal {


    public final static Logger logBiz = LoggerFactory.getLogger("logger_biz");

    int maxThreadSize = 5, minThreadSize = 2, keepTime = 3, queueSize = 100;
    ThreadPoolExecutor dangerNoticeThreadPool = new ThreadPoolExecutor(minThreadSize, maxThreadSize, keepTime, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(queueSize), new ThreadPoolExecutor.DiscardPolicy());

    @Autowired
    private BizChannelTradeOrderErrorInnerService bizChannelTradeOrderErrorInnerService;

    protected void insertDatabase(BizChannelTradeOrderError bizChannelTradeOrderError){
        Runnable runnable = () -> {
            logBiz.warn("----======【业务异常】" + "订单异常-->mchOrderNo:" + bizChannelTradeOrderError.getMchOrderNo() + ",method:" + bizChannelTradeOrderError.getMethodName() + ",msg:" + bizChannelTradeOrderError.getResp() + ",error:" + bizChannelTradeOrderError.getMsg());

            boolean exist = bizChannelTradeOrderErrorInnerService.existByMchOrderNoAndType(bizChannelTradeOrderError.getMchOrderNo(), bizChannelTradeOrderError.getType());
            if (exist) {
                bizChannelTradeOrderErrorInnerService.updateCountByMchOrderNoAndType(bizChannelTradeOrderError.getMchOrderNo(), bizChannelTradeOrderError.getType());
                return;
            }
            bizChannelTradeOrderErrorInnerService.insertSelective(bizChannelTradeOrderError);
        };
        dangerNoticeThreadPool.execute(runnable);
    }
}
