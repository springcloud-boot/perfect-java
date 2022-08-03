package xue.xiang.yi.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author : 薛向毅
 * @date : 14:27 2022/8/2
 */
public class JdkProxy {
    interface JdkInterface {
        void run();
    }
    static class JdkInterfaceImpl implements JdkInterface {

        @Override
        public void run() {
            System.out.println("我执行了哈");
        }
    }

    public static void main(String[] args) {
        //原始对象
        JdkInterface jdkInterface = new JdkInterfaceImpl();

        //代理增强
        InvocationHandler invocationHandler = new InvocationHandler() {

            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                System.out.println("---- 方法执行前打印 ----");
                // 执行原始方法
                Object result = method.invoke(jdkInterface, args);
                System.out.println("---- 方法执行后打印 ----");
                return result;
            }
        };
        // 3. 代理类
        JdkInterface jdkInterfaceProxy = (JdkInterface) Proxy.
                newProxyInstance(JdkInterface.class.getClassLoader(),
                        jdkInterface.getClass().getInterfaces(),
                        invocationHandler);
        // 4. 执行方法
        jdkInterfaceProxy.run();


    }
}
