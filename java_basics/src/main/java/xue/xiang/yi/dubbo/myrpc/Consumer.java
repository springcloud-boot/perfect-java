package xue.xiang.yi.dubbo.myrpc;

/**
 * @author : 薛向毅
 * @date : 16:29 2022/8/5
 */
public class Consumer {
    public static void main(String[] args) throws Exception {
        //服务调用者只需要设置依赖
        RpcService service = RpcFramework.refer(RpcService.class, "127.0.0.1", 8888);
        String aa = service.hello("你真好");
        System.out.println(aa);

    }
}
