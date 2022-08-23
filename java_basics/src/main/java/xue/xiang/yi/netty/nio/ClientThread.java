package xue.xiang.yi.netty.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

/**
 * 接收服务端传来的数据
 */
public class ClientThread implements Runnable{

    private Selector selector;

    public ClientThread(Selector selector) {
        this.selector = selector;
    }

    @Override
    public void run() {
        try {
            for(;;) {
                //获取 channel 数量
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
                    //如果可读状态
                    if(selectionKey.isReadable()) {
                        readOperator(selector, selectionKey);
                    }
                }
            }
        } catch(Exception e) {
        }
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
        if(readLength >0) {
            //切换读模式
            byteBuffer.flip();
            //读取内容
            message += Charset.forName("UTF-8").decode(byteBuffer);
        }
        //4 将 channel 再次注册到选择器上，监听可读状态
        socketChannel.register(selector, SelectionKey.OP_READ);

        if(message.length() > 0) {
            //打印服务端发来的数据
            System.out.println(message);
        }
    }
}

