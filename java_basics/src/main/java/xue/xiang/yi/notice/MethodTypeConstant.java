package xue.xiang.yi.notice;

/**
 * @DATE: 2020/11/30
 * @AUTHOR: 薛向毅
 * desc:
 **/
public class MethodTypeConstant {
    /**
     * 匹配不到,只会打印日志
     */
    public final static String DEFAULT = "DEFAULT";
    /**
     * 他方交易订单下单
     */
    public final static String THIRD_ORDER = "THIRD_ORDER";
    /**
     * 我方交易订单下单
     */
    public final static String OWNER_ORDER = "OWNER_ORDER";

    /**
     * 我方交易订单下单结果查询
     */
    public final static String OWNER_ORDER_QUERY = "OWNER_ORDER_QUERY";

    /**
     * 我方交易订单下单结果查询
     */
    public final static String SYNC_ORDER = "SYNC_ORDER";

}