package de.lbe.sandbox.netty.reconnect;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cartelsol.commons.lib.netty.client.ChannelListener;
import com.cartelsol.commons.lib.netty.client.ConnectionListener;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class ReconnectingNettyClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReconnectingNettyClient.class);

    private Bootstrap bootstrap = new Bootstrap();
    private SocketAddress addr_;
    private Channel channel_;

    private final ChannelListener reconnect;

    public ReconnectingNettyClient(String host, int port) {
        this(new InetSocketAddress(host, port));
    }

    public ReconnectingNettyClient(SocketAddress addr) {
        this.addr_ = addr;
        this.reconnect = new ChannelListener(this::connect);
        this.reconnect.setConnectionListener(new ConnectionListener() {

            @Override
            public void onConnectionSuccessful(Channel channel) {
                connectionEstablished();
            }

            @Override
            public void onConnectionFailed(Throwable cause) {
                // do nothing
            }

            @Override
            public void onConnectionClosed() {
                connectionLost();
            }
        });
        reconnect.setReconnectIntervalInMillis(10);

        bootstrap.group(new NioEventLoopGroup(1));
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(createNMMessageHandler());
            }
        });
    }

    public void send(String msg) throws IOException {
        if (channel_ != null && channel_.isActive()) {
            ByteBuf buf = channel_.alloc().buffer().writeBytes(msg.getBytes());
            channel_.writeAndFlush(buf);
        } else {
            throw new IOException("Can't send message to inactive connection");
        }
    }

    public void close() throws InterruptedException {
        this.reconnect.close();
        channel_.close().sync();
    }

    public void connect() {
        ChannelFuture f = bootstrap.connect(addr_);
        this.channel_ = f.channel();
        f.addListener(reconnect);
    }

    private ChannelHandler createNMMessageHandler() {
        return new ChannelInboundHandlerAdapter() {
            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) {
                ByteBuf buf = (ByteBuf) msg;
                int n = buf.readableBytes();
                if (n > 0) {
                    byte[] b = new byte[n];
                    buf.readBytes(b);
                    LOGGER.info("Get message: {}", new String(b));
                }
            }

        };
    }

    private void connectionLost() {
        System.out.println("connectionLost()");
    }

    private void connectionEstablished() {
        try {
            send("hello");
        } catch (IOException e) {
            LOGGER.error("Error while sending request", e);
        }
    }
}
