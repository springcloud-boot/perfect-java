package xue.xiang.yi.dubbo.myrpc;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author : 薛向毅
 * @date : 16:18 2022/8/5
 *
 * 实现服务提供和暴露
 */
public class RpcFramework {


    public static void export(Object service, int port) throws Exception {
        ServerSocket server = new ServerSocket(port);
        while(true) {
            Socket socket = server.accept();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        //反序列化
                        ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
                        String methodName = (String) input.readObject();
                        Class<?>[] parameterTypes = (Class<?>[]) input.readObject(); //参数类型
                        Object[] arguments = new Object[0]; //参数
                        arguments = (Object[]) input.readObject();

                        Method method = service.getClass().getMethod(methodName, parameterTypes);  //找到方法
                        Object result = method.invoke(service, arguments); //调用方法
                        // 返回结果
                        ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                     output.writeObject(result);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    public static <T> T refer (Class<T> interfaceClass, String host, int port) throws Exception {
        return  (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class<?>[] {interfaceClass},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] arguments) throws Throwable {
                        Socket socket = new Socket(host, port);  //指定 provider 的 ip 和端口
                        ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                        output.writeObject(method.getName());  //传方法名
                        output.writeObject(method.getParameterTypes());  //传参数类型
                        output.writeObject(arguments);  //传参数值

                        ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
                        Object result = input.readObject();  //读取结果
                        return result;
                    }
                });
    }

}
