package de.lbe.sandbox.netty.reconnect;

import org.junit.Test;

public class StartServerTest {

    @Test
    public void testStartServer() throws Exception {
        Server server = new Server(Server.PORT);
        server.start();
    }
}
