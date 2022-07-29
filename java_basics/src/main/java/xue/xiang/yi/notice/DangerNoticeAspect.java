package xue.xiang.yi.notice;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @DATE: 2020/11/30
 * @AUTHOR: 薛向毅
 * desc: 交易时异常通知切面. 注:如果异常突然增多,线程处理不过来,则会丢去异常记录.
 **/
@Component
@Aspect
public class DangerNoticeAspect {
    public final static Logger logBiz = LoggerFactory.getLogger("logger_biz");

//    @Autowired
//    private List<DealInterface> dealInterfaces;

    @Around("@annotation(cc.eslink.paycenter.gateway.notice.DangerNotice)")
    public Object orderAround(ProceedingJoinPoint joinPoint) throws Throwable {
        //获取方法签名
        MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();
        //获得方法
        Method method = methodSignature.getMethod();
        //获得方法上面的注解
        DangerNotice dangerNotice = method.getAnnotation(DangerNotice.class);
        // ****真正执行记录异常的类**** 重点步骤
//        DealInterface realDealInterface = getRealDealClass(dangerNotice.methodType());

        long startTime = System.currentTimeMillis();
        Object proceed = null;
        try {
            proceed = joinPoint.proceed();
        } catch (Exception e) {
            try {
//                realDealInterface.errorDispose(null);
            } catch (Exception e1) {
                logBiz.error("入库报错的异常失败", e);
            }
            throw e;
        }
//        DealParamBean pramBean = null;

        long endTime = System.currentTimeMillis();
        //是否超时,超时说明调用者已经拿不到响应了
        boolean timeOut = (endTime - startTime) > dangerNotice.timeOutMillisecond();
        //超时记录
        if (timeOut) {
            try {
//                realDealInterface.timeoutDispose(pramBean);
            } catch (Exception e) {
                logBiz.error("入库超时的异常失败", e);
            }
        }
        //失败处理逻辑
        try {
//            realDealInterface.isFailDisponse(pramBean);
        } catch (Exception e) {
            logBiz.error("入库响应失败的异常失败", e);
        }
        return proceed;
    }

    /**
     * 必然有处理类,最不济返回一个默认记录日志的
     * @param methodType
     * @return
     */
//    private DealInterface getRealDealClass(String methodType) {
//        for (DealInterface dealInterface : dealInterfaces) {
//            if (dealInterface.isSupport(methodType)) {
//                return dealInterface;
//            }
//        }
//        return new DefaultDeal();
//    }


}
