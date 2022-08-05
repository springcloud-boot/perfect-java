package xue.xiang.yi.dubbo.myrpc;

/**
 * @author : 薛向毅
 * @date : 16:29 2022/8/5
 */
public class Provider {
    public static void main(String[] args) throws Exception {
        RpcService service = new RpcServiceImpl ();
        RpcFramework.export(service, 8888);
    }
}
