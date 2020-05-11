package de.lbe.sandbox.sse.client;

import javax.ws.rs.client.ClientBuilder;

import org.glassfish.jersey.media.sse.SseFeature;

/**
 * @author lbeuster
 */
public class SseClientBuilder {

    public ClientBuilder builder() {
        return ClientBuilder.newBuilder().register(SseFeature.class);
    }
}
