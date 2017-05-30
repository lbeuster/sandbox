package de.lbe.sandbox.netty;

import java.io.Closeable;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class TestClient implements Closeable {

    private final int port;

    private final EventLoopGroup workerGroup;

    private final ChannelHandler[] handlers;

    private Channel channel;

    public TestClient(int port, ChannelHandler... handlers) {
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
            this.channel = f.channel();
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void close() {
        channel.close();
        workerGroup.shutdownGracefully();
    }

    public Channel getChannel() {
        return channel;
    }
}