//package xue.xiang.yi.notice.deal;
//
//import org.springframework.stereotype.Component;
//import xue.xiang.yi.notice.MethodTypeConstant;
//
//import java.util.Optional;
//
///**
// * @DATE: 2020/11/30
// * @AUTHOR: 薛向毅
// * desc: 下单异常通知处理. 注:如果异常突然增多,线程处理不过来,则会丢去异常记录.
// **/
//@Component
//public class OwnerOrderDeal extends BaseDeal implements DealInterface {
//
//
//    @Override
//    public boolean isSupport(String methodTypeName) {
//        if (MethodTypeConstant.OWNER_ORDER.equals(methodTypeName)) {
//            return true;
//        }
//        return false;
//    }
//
//
//    @Override
//    public void isFailDisponse(DealParamBean pramBean) {
////        ChannelUnifiedOrderResp channelUnifiedOrderResp = (ChannelUnifiedOrderResp)pramBean.getRespMsg();
////        if (ChannelUnifiedOrderResp.RESULTSUCCESS.equals(channelUnifiedOrderResp.getResultCode())) {
////            return;
////        }
////
////        ChannelUnifiedOrderReq channelUnifiedOrderReq = getArgs(pramBean.getJoinPoint().getArgs());
////        BizChannelTradeOrderError bean = new BizChannelTradeOrderError();
////        bean.setMchOrderNo(Optional.ofNullable(channelUnifiedOrderResp.getMchOrderNo()).orElseGet(()->channelUnifiedOrderResp.getTransactionId()));
////        bean.setTransactionId(channelUnifiedOrderReq.getTransactionId());
////        bean.setArgs(JsonUtil.encode(channelUnifiedOrderReq));
////        bean.setCreateDate(channelUnifiedOrderReq.getCreateDate());
////        bean.setMethodName(pramBean.getMethod().getName());
////        bean.setRefrerenceName(pramBean.getJoinPoint().getTarget().getClass().getName());
////        bean.setResp(JsonUtil.encode(channelUnifiedOrderResp));
////        bean.setType(TradeOrderErrorTypeConstant.FAIL);
////        insertDatabase(bean);
//    }
//
//
//    //根据自己的方法去找,这个方法入参是这个
//    private ChannelUnifiedOrderReq getArgs(Object[] args) {
//        return (ChannelUnifiedOrderReq)args[0];
//    }
//
//
//    @Override
//    public void timeoutDispose(DealParamBean pramBean) {
//        ChannelUnifiedOrderResp channelUnifiedOrderResp = (ChannelUnifiedOrderResp)pramBean.getRespMsg();
//        ChannelUnifiedOrderReq channelUnifiedOrderReq = getArgs(pramBean.getJoinPoint().getArgs());
//        BizChannelTradeOrderError bean = new BizChannelTradeOrderError();
//        bean.setMchOrderNo(channelUnifiedOrderResp.getMchOrderNo());
//        bean.setTransactionId(channelUnifiedOrderReq.getTransactionId());
//        bean.setArgs(JsonUtil.encode(channelUnifiedOrderReq));
//        bean.setCreateDate(channelUnifiedOrderReq.getCreateDate());
//        bean.setMethodName(pramBean.getMethod().getName());
//        bean.setRefrerenceName(pramBean.getJoinPoint().getTarget().getClass().getName());
//        bean.setResp(JsonUtil.encode(channelUnifiedOrderResp));
//        bean.setType(TradeOrderErrorTypeConstant.TIME_OUT);
//        insertDatabase(bean);
//    }
//
//    @Override
//    public void errorDispose(DealParamBean pramBean) {
//        ChannelUnifiedOrderReq channelUnifiedOrderReq = getArgs(pramBean.getJoinPoint().getArgs());
//        BizChannelTradeOrderError bean = new BizChannelTradeOrderError();
//        bean.setMchOrderNo(channelUnifiedOrderReq.getTransactionId());
//        bean.setTransactionId(channelUnifiedOrderReq.getTransactionId());
//        bean.setArgs(JsonUtil.encode(channelUnifiedOrderReq));
//        bean.setCreateDate(channelUnifiedOrderReq.getCreateDate());
//        bean.setMethodName(pramBean.getMethod().getName());
//        bean.setRefrerenceName(pramBean.getJoinPoint().getTarget().getClass().getName());
//        bean.setType(TradeOrderErrorTypeConstant.ERROR);
//        bean.setMsg(pramBean.getError().getMessage());
//        insertDatabase(bean);
//    }
//
//}
