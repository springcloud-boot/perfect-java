package xue.xiang.yi.notice.deal;


/**
 * @DATE: 2020/11/30
 * @AUTHOR: 薛向毅
 * desc: 异常处理接口
 **/
public interface DealInterface {
    /**
     * 是否支持(拿到真实执行类)
     * @param methodTypeName 见
     *              @see cc.eslink.paycenter.gateway.notice.MethodTypeConstant
     *
     */
    public boolean isSupport(String methodTypeName);


    /**
     * 超时处理
     */
    public void timeoutDispose(DealParamBean pramBean);

    /**
     * 失败处理
     */
    public void isFailDisponse(DealParamBean pramBean);

    /**
     * 错误处理
     */
    public void errorDispose(DealParamBean pramBean);
}
