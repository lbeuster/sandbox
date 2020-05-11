package de.lbe.sandbox.sse.client;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.glassfish.jersey.media.sse.EventListener;
import org.glassfish.jersey.media.sse.EventSource;
import org.junit.jupiter.api.Test;

/**
 * @author lbeuster
 */
public class OtherSseClientIT extends AbstractSseIT {

    @Test
    public void test_register_subscriber() {
        Client client = ClientBuilder.newBuilder().build();

        WebTarget target = client.target(baseUrl()).path("/sse/messages");
        // invoke http request
        EventSource eventSource = new EventSource(target, false);
        //
        // EventSource.target(target)
        // .build();
        // listening for incoming Sse Events
        EventListener listener = inboundEvent -> {
            System.out.println(inboundEvent.getName() + "; " + inboundEvent.readData(String.class));
        };
        eventSource.register(listener);
        System.out.println("before open");
        eventSource.open();
        System.out.println("after open");

        eventSource.close();
    }

}
