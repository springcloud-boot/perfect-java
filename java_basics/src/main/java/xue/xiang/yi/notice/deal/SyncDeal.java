package xue.xiang.yi.notice.deal;

import cc.eslink.paycenter.gateway.domain.BizChannelTradeOrderError;
import cc.eslink.paycenter.gateway.domain.ChannelTradeOrder;
import cc.eslink.paycenter.gateway.domain.bizsync.BizSyncResponse;
import cc.eslink.paycenter.gateway.notice.MethodTypeConstant;
import cc.eslink.paycenter.gateway.util.JsonUtil;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @DATE: 2020/11/30
 * @AUTHOR: 薛向毅
 * desc: 下单异常通知处理. 注:如果异常突然增多,线程处理不过来,则会丢去异常记录.
 **/
@Component
public class SyncDeal extends BaseDeal implements DealInterface {


    @Override
    public boolean isSupport(String methodTypeName) {
        if (MethodTypeConstant.SYNC_ORDER.equals(methodTypeName)) {
            return true;
        }
        return false;
    }


    @Override
    public void isFailDisponse(DealParamBean pramBean) {
        BizSyncResponse resp = (BizSyncResponse)pramBean.getRespMsg();
        //封装一个必定不为空的结果
        BizSyncResponse bizSyncResponse = Optional.ofNullable(resp).orElseGet(() -> {
            return BizSyncResponse.getFail(BizSyncResponse.EX_COMMUNICATION, "同步时,调用接口响应为null或者报错");
        });
        if (BizSyncResponse.SUCCESS.equals(bizSyncResponse.getResultCode())) {
            return;
        }

        ChannelTradeOrder channelTradeOrder = getArgs(pramBean.getJoinPoint().getArgs());
        BizChannelTradeOrderError bean = new BizChannelTradeOrderError();
        bean.setMchOrderNo(channelTradeOrder.getMchOrderNo());
        bean.setTransactionId(channelTradeOrder.getTransactionId());
        bean.setArgs(JsonUtil.encode(channelTradeOrder));
        bean.setCreateDate(channelTradeOrder.getCreateDate());
        bean.setMethodName(pramBean.getMethod().getName());
        bean.setRefrerenceName(pramBean.getJoinPoint().getTarget().getClass().getName());
        bean.setResp(JsonUtil.encode(bizSyncResponse));
        bean.setType(TradeOrderErrorTypeConstant.FAIL);
        insertDatabase(bean);
    }


    //根据自己的方法去找,这个方法入参是这个
    private ChannelTradeOrder getArgs(Object[] args) {
        return (ChannelTradeOrder)args[0];
    }


    @Override
    public void timeoutDispose(DealParamBean pramBean) {
        BizSyncResponse resp = (BizSyncResponse)pramBean.getRespMsg();
        //封装一个必定不为空的结果
        BizSyncResponse bizSyncResponse = Optional.ofNullable(resp).orElseGet(() -> {
            return BizSyncResponse.getFail(BizSyncResponse.EX_COMMUNICATION, "同步时,调用接口响应为null或者报错");
        });

        ChannelTradeOrder channelTradeOrder = getArgs(pramBean.getJoinPoint().getArgs());
        BizChannelTradeOrderError bean = new BizChannelTradeOrderError();
        bean.setMchOrderNo(channelTradeOrder.getMchOrderNo());
        bean.setTransactionId(channelTradeOrder.getTransactionId());
        bean.setArgs(JsonUtil.encode(channelTradeOrder));
        bean.setCreateDate(channelTradeOrder.getCreateDate());
        bean.setMethodName(pramBean.getMethod().getName());
        bean.setRefrerenceName(pramBean.getJoinPoint().getTarget().getClass().getName());
        bean.setResp(JsonUtil.encode(bizSyncResponse));
        bean.setType(TradeOrderErrorTypeConstant.TIME_OUT);
        insertDatabase(bean);
    }

    @Override
    public void errorDispose(DealParamBean pramBean) {
        ChannelTradeOrder channelTradeOrder = getArgs(pramBean.getJoinPoint().getArgs());
        BizChannelTradeOrderError bean = new BizChannelTradeOrderError();
        bean.setMchOrderNo(channelTradeOrder.getMchOrderNo());
        bean.setTransactionId(channelTradeOrder.getTransactionId());
        bean.setArgs(JsonUtil.encode(channelTradeOrder));
        bean.setCreateDate(channelTradeOrder.getCreateDate());
        bean.setMethodName(pramBean.getMethod().getName());
        bean.setRefrerenceName(pramBean.getJoinPoint().getTarget().getClass().getName());
        bean.setType(TradeOrderErrorTypeConstant.ERROR);
        bean.setMsg(pramBean.getError().getMessage());
        insertDatabase(bean);
    }

}
