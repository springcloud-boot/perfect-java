package xue.xiang.yi.netty.demo.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ServerHandler
                       extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        System.out.println("Client Address ====== " + ctx.channel().remoteAddress()+ msg+ "--"+Thread.currentThread().getName());
        ctx.channel().writeAndFlush("from server:" + UUID.randomUUID());
        TimeUnit.MILLISECONDS.sleep(500);
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,
                                Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}