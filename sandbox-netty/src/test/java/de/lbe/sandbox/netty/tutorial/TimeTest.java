package de.lbe.sandbox.netty.tutorial;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import org.junit.Test;

import com.cartelsol.commons.lib.io.Sockets;
import com.cartelsol.commons.lib.test.junit.AbstractJUnitTest;

import de.lbe.sandbox.netty.TestClient;
import de.lbe.sandbox.netty.TestServer;

/**
 * @author lbeuster
 */
public class TimeTest extends AbstractJUnitTest {

    @Test
    public void testTime() throws Exception {
        int port = Sockets.findAvailableTCPPort();

        TestServer server = new TestServer(port, new TimeEncoder(), new TimeServerHandler());
        new Thread(server::run).start();

        AtomicReference<UnixTimestamp> dateRef = new AtomicReference<>();
        Consumer<UnixTimestamp> consumer = date -> {
            System.out.println(date);
            dateRef.set(date);
        };

        TestClient client = new TestClient(port, new TimeDecoder(), new TimeClientHandler(consumer));
        client.run();
        Thread.sleep(1000);
        client.close();

        server.close();
        assertNotNull(dateRef.get());
    }
}
