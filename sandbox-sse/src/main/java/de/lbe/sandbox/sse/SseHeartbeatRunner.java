package de.lbe.sandbox.sse;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * @author lbeuster
 */
public class SseHeartbeatRunner {

    private static final Logger LOGGER = SseLogger.LOGGER;

    private final SseConnectionManager connectionManager;

    private final HeartbeatSender heartbeatSender;

    private final Thread thread;

    private final long intervalInMillis;

    /**
     *
     */
    public SseHeartbeatRunner(SseConnectionManager connectionManager, HeartbeatSender heartbeatSender, long intervalInMillis) {
        this.connectionManager = connectionManager;
        this.intervalInMillis = intervalInMillis;
        this.heartbeatSender = heartbeatSender;

        thread = new Thread(() -> run(), "SseHeartbeat");
        thread.setDaemon(true);
    }

    /**
     *
     */
    @PostConstruct
    public void start() {
        this.thread.start();
    }

    /**
     *
     */
    @PreDestroy
    public void stop() {
        if (thread != null) {
            thread.interrupt();
        }
    }

    // @PostConstruct
    private void run() {

        while (true) { // NOSONAR: Loops should not be infinite

            // sleep
            try {
                Thread.sleep(intervalInMillis); // NOSONAR: "Thread.sleep" should not be used in tests (squid:S2925)
            } catch (@SuppressWarnings("unused") InterruptedException ex) { // NOSONAR: "InterruptedException" should not be ignored
                LOGGER.info("Got interrupt, finishing SSE heartbeat");
                return;
            }

            // heartbeat
            try {
                // connectionManager.scheduleForAll((key, emitter) -> heartbeatSender.send(emitter));
            } catch (Exception ex) {
                // should not be necessary but to be on the safe side
                LOGGER.error("Caught exception during heartbeat", ex);
            }
        }
    }

    /**
     *
     */
    @FunctionalInterface
    public interface HeartbeatSender {

        void send(SseEmitter emitter) throws IOException;
    }
}
