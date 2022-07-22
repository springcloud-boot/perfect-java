package xue.xiang.yi.notice.deal;


/**
 * @DATE: 2020/11/30
 * @AUTHOR: 薛向毅
 * desc: 默认的处理方式,只打印日志!不做任何处理,只能new出来
 **/
public class DefaultDeal extends BaseDeal implements DealInterface{
    @Override
    public boolean isSupport(String methodTypeName) {
        return false;
    }

    @Override
    public void timeoutDispose(DealParamBean pramBean) {
        logBiz.error("方法执行超时; methodName:{} , args:{} , resp:{}", pramBean.getMethod().getName(), JsonUtil.encode(pramBean.getJoinPoint().getArgs()), JsonUtil.encode((Object[]) pramBean.getRespMsg()));
    }

    @Override
    public void isFailDisponse(DealParamBean pramBean) {
        logBiz.error("方法执行失败; methodName:{} , args:{} , resp:{}", pramBean.getMethod().getName(), JsonUtil.encode(pramBean.getJoinPoint().getArgs()), JsonUtil.encode((Object[]) pramBean.getRespMsg()));
    }

    @Override
    public void errorDispose(DealParamBean pramBean) {
        logBiz.error("方法执行报错; methodName:{} , args:{} , error:{}", pramBean.getMethod().getName(), JsonUtil.encode(pramBean.getJoinPoint().getArgs()), JsonUtil.encode(pramBean.getError()));
    }
}
