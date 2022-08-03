package xue.xiang.yi.netty.demo.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ClientHandler
               extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        System.out.println(msg);
        ctx.channel().writeAndFlush("from client啊啊啊:" + UUID.randomUUID());
        TimeUnit.MILLISECONDS.sleep(5000);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx)
            throws Exception {
        ctx.channel().writeAndFlush("from client：begin talking");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,
                                Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}