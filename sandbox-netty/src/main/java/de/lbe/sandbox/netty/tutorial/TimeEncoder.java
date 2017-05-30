package de.lbe.sandbox.netty.tutorial;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class TimeEncoder extends MessageToByteEncoder<UnixTimestamp> {
    @Override
    protected void encode(ChannelHandlerContext ctx, UnixTimestamp msg, ByteBuf out) {
        out.writeInt((int) msg.value());
    }
}