package xue.xiang.yi.netty.nio;

import java.io.IOException;

/**
 * 客户端A
 */
public class AClient {

    public static void main(String[] args) {
        try {
            new ChatClient().startClient("A");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
