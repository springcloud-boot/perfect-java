package xue.xiang.yi.notice.deal;

import cc.eslink.paycenter.gateway.domain.BizChannelTradeOrderError;
import cc.eslink.paycenter.gateway.domain.request.ChannelUnifiedOrderReq;
import cc.eslink.paycenter.gateway.domain.response.ChannelThridTradeResp;
import cc.eslink.paycenter.gateway.domain.response.ChannelUnifiedOrderResp;
import cc.eslink.paycenter.gateway.notice.MethodTypeConstant;
import cc.eslink.paycenter.gateway.util.JsonUtil;
import org.springframework.stereotype.Component;

/**
 * @DATE: 2020/11/30
 * @AUTHOR: 薛向毅
 * desc: 三方下单异常通知处理. 注:如果异常突然增多,线程处理不过来,则会丢去异常记录.
 **/
@Component
public class ThirdOrderDeal extends BaseDeal implements DealInterface {


    @Override
    public boolean isSupport(String methodTypeName) {
        if (MethodTypeConstant.THIRD_ORDER.equals(methodTypeName)) {
            return true;
        }
        return false;
    }


    @Override
    public void isFailDisponse(DealParamBean pramBean) {
        ChannelThridTradeResp channelThridTradeResp = (ChannelThridTradeResp)pramBean.getRespMsg();
        if (ChannelUnifiedOrderResp.RESULTSUCCESS.equals(channelThridTradeResp.getResultCode())) {
            return;
        }

        ChannelUnifiedOrderReq channelUnifiedOrderReq = getArgs(pramBean.getJoinPoint().getArgs());
        BizChannelTradeOrderError bean = new BizChannelTradeOrderError();
        bean.setMchOrderNo(channelUnifiedOrderReq.getTransactionId());
        bean.setTransactionId(channelUnifiedOrderReq.getTransactionId());
        bean.setArgs(JsonUtil.encode(channelUnifiedOrderReq));
        bean.setCreateDate(channelThridTradeResp.getOrderCreateDate());
        bean.setMethodName(pramBean.getMethod().getName());
        bean.setRefrerenceName(pramBean.getJoinPoint().getTarget().getClass().getName());
        bean.setResp(JsonUtil.encode(channelThridTradeResp));
        bean.setType(TradeOrderErrorTypeConstant.FAIL);
        insertDatabase(bean);
    }


    //根据自己的方法去找,这个方法入参是这个
    private ChannelUnifiedOrderReq getArgs(Object[] args) {
        return (ChannelUnifiedOrderReq)args[0];
    }


    @Override
    public void timeoutDispose(DealParamBean pramBean) {
        ChannelThridTradeResp channelThridTradeResp = (ChannelThridTradeResp)pramBean.getRespMsg();
        ChannelUnifiedOrderReq channelUnifiedOrderReq = getArgs(pramBean.getJoinPoint().getArgs());
        BizChannelTradeOrderError bean = new BizChannelTradeOrderError();
        bean.setMchOrderNo(channelUnifiedOrderReq.getTransactionId());
        bean.setTransactionId(channelUnifiedOrderReq.getTransactionId());
        bean.setArgs(JsonUtil.encode(channelUnifiedOrderReq));
        bean.setCreateDate(channelThridTradeResp.getOrderCreateDate());
        bean.setMethodName(pramBean.getMethod().getName());
        bean.setRefrerenceName(pramBean.getJoinPoint().getTarget().getClass().getName());
        bean.setResp(JsonUtil.encode(channelThridTradeResp));
        bean.setType(TradeOrderErrorTypeConstant.TIME_OUT);
        insertDatabase(bean);
    }

    @Override
    public void errorDispose(DealParamBean pramBean) {
        ChannelUnifiedOrderReq channelUnifiedOrderReq = getArgs(pramBean.getJoinPoint().getArgs());
        BizChannelTradeOrderError bean = new BizChannelTradeOrderError();
        bean.setMchOrderNo(channelUnifiedOrderReq.getTransactionId());
        bean.setTransactionId(channelUnifiedOrderReq.getTransactionId());
        bean.setArgs(JsonUtil.encode(channelUnifiedOrderReq));
        //这时报错了,拿不到响应; 故只记录了接口调用时间,一般不会差距太远
        bean.setCreateDate(channelUnifiedOrderReq.getCreateDate());
        bean.setMethodName(pramBean.getMethod().getName());
        bean.setRefrerenceName(pramBean.getJoinPoint().getTarget().getClass().getName());
        bean.setType(TradeOrderErrorTypeConstant.ERROR);
        bean.setMsg(pramBean.getError().getMessage());
        insertDatabase(bean);
    }

}
