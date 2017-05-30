package de.lbe.sandbox.netty;

import java.nio.charset.StandardCharsets;

import org.junit.Test;

import com.cartelsol.commons.lib.io.Sockets;
import com.cartelsol.commons.lib.test.junit.AbstractJUnitTest;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.UnpooledByteBufAllocator;

/**
 * @author lbeuster
 */
public class SendTest extends AbstractJUnitTest {

    @Test
    public void testTime() throws Exception {
        int port = Sockets.findAvailableTCPPort();

        TestServer server = new TestServer(port, new PrintServerHandler());
        new Thread(server::run).start();

        TestClient client = new TestClient(port);
        client.run();

        ByteBuf buffer = UnpooledByteBufAllocator.DEFAULT.buffer();
        buffer.writeCharSequence("hallo", StandardCharsets.UTF_8);
        client.getChannel().writeAndFlush(buffer).sync();

        Thread.sleep(2000);

        buffer = UnpooledByteBufAllocator.DEFAULT.buffer();
        buffer.writeCharSequence("hallo", StandardCharsets.UTF_8);
        client.getChannel().writeAndFlush(buffer).sync();

        client.close();

        server.close();
    }
}
