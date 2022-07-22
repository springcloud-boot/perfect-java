//package xue.xiang.yi.notice.deal;
//
//import cc.eslink.paycenter.gateway.notice.DangerNotice;
//import org.aspectj.lang.ProceedingJoinPoint;
//
//import java.lang.reflect.Method;
//
///**
// * @DATE: 2020/11/30
// * @AUTHOR: 薛向毅
// * desc: 处理类需要的参数实体类
// **/
//public class DealParamBean {
//    ProceedingJoinPoint joinPoint;
//    Method method;
////    DangerNotice dangerNotice;
//    Object respMsg;
//    Exception error;
//
//    public Object[] getError() {
//        return error;
//    }
//
//    public void setError(Exception error) {
//        this.error = error;
//    }
//
//    public DealParamBean(ProceedingJoinPoint joinPoint, Method method, DangerNotice dangerNotice, Object respMsg) {
//        this.joinPoint = joinPoint;
//        this.method = method;
//        this.dangerNotice = dangerNotice;
//        this.respMsg = respMsg;
//    }
//
//    public DealParamBean(ProceedingJoinPoint joinPoint, Method method, DangerNotice dangerNotice, Exception error) {
//        this.joinPoint = joinPoint;
//        this.method = method;
//        this.dangerNotice = dangerNotice;
//        this.error = error;
//    }
//
//    public ProceedingJoinPoint getJoinPoint() {
//        return joinPoint;
//    }
//
//    public void setJoinPoint(ProceedingJoinPoint joinPoint) {
//        this.joinPoint = joinPoint;
//    }
//
//    public Method getMethod() {
//        return method;
//    }
//
//    public void setMethod(Method method) {
//        this.method = method;
//    }
//
//    public DangerNotice getDangerNotice() {
//        return dangerNotice;
//    }
//
//    public void setDangerNotice(DangerNotice dangerNotice) {
//        this.dangerNotice = dangerNotice;
//    }
//
//    public Object getRespMsg() {
//        return respMsg;
//    }
//
//    public void setRespMsg(Object respMsg) {
//        this.respMsg = respMsg;
//    }
//}
