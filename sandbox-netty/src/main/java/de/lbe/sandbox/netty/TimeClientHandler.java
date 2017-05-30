package de.lbe.sandbox.netty;

import java.util.function.Consumer;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class TimeClientHandler extends ChannelInboundHandlerAdapter {

    private final Consumer<UnixTimestamp> callback;

    public TimeClientHandler(Consumer<UnixTimestamp> callback) {
        this.callback = callback;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        UnixTimestamp m = (UnixTimestamp) msg;
        callback.accept(m);
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}