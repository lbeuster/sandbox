package de.lbe.sandbox.netty;

import java.io.Closeable;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Discards any incoming data.
 */
public class TestServer implements Closeable {

    private int port;

    private final ChannelHandler[] handlers;

    private final EventLoopGroup bossGroup;
    private final EventLoopGroup workerGroup;

    public TestServer(int port, ChannelHandler... handlers) {
        this.port = port;
        bossGroup = new NioEventLoopGroup(1); // number of threads
        workerGroup = new NioEventLoopGroup(1);
        this.handlers = handlers;
    }

    @Override
    public void close() {
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
    }

    public void run() {
        try {
            ServerBootstrap server = new ServerBootstrap();
            // @formatter:off
            server
                .group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(handlers);
                    }
                })
                .option(ChannelOption.SO_BACKLOG, 128).childOption(ChannelOption.SO_KEEPALIVE, true);
            // @formatter:on

            // Bind and start to accept incoming connections.
            ChannelFuture f = server.bind(port).sync(); // (7)

            // Wait until the server socket is closed.
            // In this example, this does not happen, but you can do that to gracefully
            // shut down your server.
            f.channel().closeFuture().sync();
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }
}