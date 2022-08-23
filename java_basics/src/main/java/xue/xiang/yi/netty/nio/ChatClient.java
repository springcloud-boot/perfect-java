package xue.xiang.yi.netty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Scanner;

/**
 * 多人聊天室 客户端代码
 */
public class ChatClient {

    //启动客户端方法
    public void startClient(String name) throws IOException {
        //连接服务端
        SocketChannel socketChannel =
                SocketChannel.open(new InetSocketAddress("127.0.0.1",8000));
        //接收服务端响应数据
        Selector selector = Selector.open();
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
        //创建线程
        new Thread(new ClientThread(selector)).start();

        //向服务器端发送消息
        Scanner scanner = new Scanner(System.in);
        while(scanner.hasNextLine()) {
            String msg = scanner.nextLine();
            if(msg.length() > 0) {
                socketChannel.write(Charset.forName("UTF-8").encode(name +" : " +msg));
            }
        }
    }
}
