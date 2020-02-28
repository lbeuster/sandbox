package de.lbe.sandbox.netty.reconnect;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReconnectingNettyClientTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReconnectingNettyClientTest.class);

    @Test
    public void testStartClient() throws Exception {

        ReconnectingNettyClient conn = new ReconnectingNettyClient("127.0.0.1", Server.PORT);
        conn.connect();
        int counter = 1;
        for (int i = 0; i < 5000; i++) {
            // while (true) {
            Thread.sleep(1000);
            try {
                conn.send("Message" + counter++ + "\n");
            } catch (Exception ex) {
                LOGGER.error("Error sending message " + ex);
            }
        }
        conn.close();
        Thread.sleep(10000);
    }
}
