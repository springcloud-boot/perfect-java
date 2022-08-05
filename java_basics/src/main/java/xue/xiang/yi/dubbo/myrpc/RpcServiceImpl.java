package xue.xiang.yi.dubbo.myrpc;

/**
 * @author : 薛向毅
 * @date : 16:17 2022/8/5
 */
public class RpcServiceImpl implements RpcService{
    @Override
    public String hello(String name) {
        return "调用成功，名字:" + name;
    }
}
