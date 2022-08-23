package xue.xiang.yi.netty.nio;

import java.io.IOException;

/**
 * 客户端B
 */
public class BClient {

    public static void main(String[] args) {
        try {
            new ChatClient().startClient("B");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
