package de.lbe.sandbox.netty;

import java.io.Closeable;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class TimeClient implements Closeable {

    private final int port;

    private final EventLoopGroup workerGroup;

    private final ChannelHandler[] handlers;

    public TimeClient(int port, ChannelHandler... handlers) {
        this.port = port;
        this.workerGroup = new NioEventLoopGroup();
        this.handlers = handlers;
    }

    public void run() {
        try {
            Bootstrap b = new Bootstrap(); // (1)
            b.group(workerGroup); // (2)
            b.channel(NioSocketChannel.class); // (3)
            b.option(ChannelOption.SO_KEEPALIVE, true); // (4)
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(handlers);
                }
            });

            // Start the client.
            ChannelFuture f = b.connect("localhost", port).sync(); // (5)

            // Wait until the connection is closed.
            f.channel().closeFuture().sync();
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void close() {
        workerGroup.shutdownGracefully();
    }
}