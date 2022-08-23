package xue.xiang.yi.netty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

/**
 * @author : 薛向毅
 * @date : 10:01 2022/8/23
 */
public class ChatServer {
    public static void main(String[] args) throws IOException {
        new ChatServer().startServer();
    }

    //服务器端启动的方法
    public void startServer() throws IOException {
        //1 创建 Selector 选择器
        Selector selector = Selector.open();
        //2 创建 ServerSocketChannel 通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //3 为 ServerSocketChannel 通道绑定监听端口
        serverSocketChannel.bind(new InetSocketAddress(8000));
        //设置非阻塞模式
        serverSocketChannel.configureBlocking(false);
        //4 把通道注册到 selector 选择器上
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("服务器已经启动成功了");

        //5 循环，等待有新链接接入
        //while(true)
        for(;;) {
            // 查询出已经就绪的通道操作，获取 channel 数量
            //（SocketChannel每个通道都有其目前的状态：SelectionKey）
            int readChannels = selector.select();
            if(readChannels == 0) {
                continue;
            }
            //获取可用的 channel
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            //遍历集合
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                //移除 set 集合当前 selectionKey
                iterator.remove();

                //6 根据就绪状态，调用对应方法实现具体业务操作
                //6.1 如果 accept 状态
                if(selectionKey.isAcceptable()) {
                    acceptOperator(serverSocketChannel, selector);
                }
                //6.2 如果可读状态
                if(selectionKey.isReadable()) {
                    readOperator(selector, selectionKey);
                }
            }
        }
    }
    //处理接入状态操作
    private void acceptOperator(ServerSocketChannel serverSocketChannel, Selector selector) throws IOException {
        //1 接入状态，创建 socketChannel
        SocketChannel socketChannel = serverSocketChannel.accept();
        //2 把 socketChannel 设置非阻塞模式
        socketChannel.configureBlocking(false);
        //3 把 channel 注册到 selector 选择器上，监听可读状态
        socketChannel.register(selector, SelectionKey.OP_READ);
        //4 客户端回复信息
        socketChannel.write(Charset.forName("UTF-8").encode("欢迎进入聊天室，请注意隐私安全"));
    }

    //处理可读状态操作
    private void readOperator(Selector selector, SelectionKey selectionKey) throws IOException {
        // 1 从 SelectionKey 获取到已经就绪的通道
        SocketChannel socketChannel = (SocketChannel)selectionKey.channel();
        // 2 创建 buffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        // 3 循环读取客户端消息
        int readLength = socketChannel.read(byteBuffer);
        String message = "";
        if(readLength > 0) {
            //切换读模式
            byteBuffer.flip();
            //读取到的内容
            message += Charset.forName("UTF-8").decode(byteBuffer);
        }
        // 4 将 channel 再次注册到选择器上，监听可读状态
        socketChannel.register(selector, SelectionKey.OP_READ);
        // 5 把客户端发送消息，广播到其他客户端
        if(message.length()>0) {
            //广播给其他客户端
            System.out.println(message);
            castOtherClient(message, selector, socketChannel);
        }
    }
    //广播到其他客户端
    private void castOtherClient(String message, Selector selector, SocketChannel socketChannel) throws IOException {
        //1 获取所有已经接入的socketChannel （每个socketChannel都有其目前的状态：SelectionKey）
        Set<SelectionKey> selectionKeySet = selector.keys();
        //2 循环向所有socketChannel 广播消息
        for(SelectionKey selectionKey : selectionKeySet) {
            //获取每个 channel
            Channel targetChannel = selectionKey.channel();
            //不需要给自己发送
            if(targetChannel instanceof SocketChannel && targetChannel != socketChannel) {
                ((SocketChannel)targetChannel).write(Charset.forName("UTF-8").encode(message));
            }
        }
    }

}
