package de.lbe.sandbox.sse.client;

import java.io.Closeable;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.client.WebTarget;

import org.glassfish.jersey.media.sse.EventListener;
import org.glassfish.jersey.media.sse.EventSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Verify;

/**
 * @author lbeuster
 */
public class SseTarget implements Closeable {

    private static final Logger LOGGER = LoggerFactory.getLogger(SseTarget.class);

    private final WebTarget webTarget;

    private EventSource source;

    public SseTarget(WebTarget webTarget) {
        this.webTarget = webTarget;
    }

    public void open(EventListener listener) {
        Verify.verify(this.source == null, "Already open");
        this.source = EventSource.target(webTarget).build();

        // since the original impl only logs on fine-level we log here on error level
        this.source.register(event -> {
            System.out.println("Got event: " + event);
            try {
                listener.onEvent(event);
            } catch (Exception ex) {
                LOGGER.error("Caught exception during SSE event dispatching", ex);
                throw ex;
            }
        });
        this.source.open();
    }

    @Override
    public void close() {
        if (this.source != null && this.source.isOpen()) {
            this.source.close(500, TimeUnit.MILLISECONDS);
            this.source = null;
        }
    }

    public EventSource source() {
        return this.source;
    }
}
