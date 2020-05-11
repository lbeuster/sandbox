package de.lbe.sandbox.sse.client;

import static org.awaitility.Awaitility.await;
import static org.awaitility.Durations.FIVE_SECONDS;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.client.WebTarget;

import org.glassfish.jersey.media.sse.EventSource;
import org.junit.jupiter.api.Test;

import com.cartelsol.commons.lib.spring.boot.web.error.ErrorLogger;
import com.cartelsol.commons.lib.test.junit.extensions.LoggingExtension.LogEvents;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.ThrowableProxy;

/**
 * @author lbeuster
 */
public class SseClientIT extends AbstractSseIT {

    /**
     *
     */
    @Test
    public void testConnect() throws Exception {

        // open connection
        SseTarget sseTarget = defaultSseTarget(getTestMethodName());
        List<String> receivedMessages = Collections.synchronizedList(new ArrayList<>());
        System.out.println("before open");
        sseTarget.open(event -> {
            String payload = event.readData(String.class);
            receivedMessages.add(payload);
        });
        System.out.println("after open");

        // assert
        await().atMost(FIVE_SECONDS).untilAsserted(() -> {
            assertNotNull(receivedMessages);
            assertThat(receivedMessages, not(empty()));
        });
    }

    /**
     *
     */
    @Test
    public void testExceptionHandling() throws Exception {

        // prepare logging
        disableLogging(ErrorLogger.LOGGER);
        this.logging.setLogLevel(EventSource.class, Level.FINE);
        LogEvents logEvents = this.logging.collectLogEvents(EventSource.class);

        // open connection
        WebTarget webTarget = webTarget().path("/sse/NullPointerException");
        SseTarget sseTarget = sseTarget(webTarget);
        sseTarget.open(event -> assertTrue(true));

        // the connection is automatically closed on an exception
        await().atMost(FIVE_SECONDS).untilAsserted(() -> {
            assertNotNull(sseTarget.source());
            assertFalse(sseTarget.source().isOpen());

            // we have log events
            assertThat(logEvents, hasSize(1));
            List<ILoggingEvent> events = logEvents.filterByLevel(Level.FINE);
            assertThat(events, hasSize(1));
            ILoggingEvent event = events.get(0);
            ThrowableProxy exception = (ThrowableProxy) event.getThrowableProxy();
            assertThat(exception.getThrowable(), instanceOf(InternalServerErrorException.class));
        });
    }
}
