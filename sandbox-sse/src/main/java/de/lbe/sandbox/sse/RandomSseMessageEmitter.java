package de.lbe.sandbox.sse;

import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

/**
 * @author lbeuster
 */
@Component
public class RandomSseMessageEmitter implements Runnable {

    private final SseConnectionManager connectionManager;

    private final Thread thread;

    public RandomSseMessageEmitter(SseConnectionManager sseEmitters) {
        this.connectionManager = sseEmitters;
        thread = new Thread(this, getClass().getSimpleName());
        thread.setDaemon(true);
    }

    @PostConstruct
    public void start() {
        thread.start();
    }

    @PreDestroy
    public void stop() {
        thread.interrupt();
    }

    @Override
    public void run() {

        AtomicInteger counter = new AtomicInteger();
        while (true) {

            try {
                Thread.sleep(1_000); // NOSONAR: "Thread.sleep" should not be used in tests (squid:S2925)
            } catch (@SuppressWarnings("unused") InterruptedException ex) {
                return;
            }

            String messagePostfix = "message-" + counter.incrementAndGet();
            connectionManager.scheduleForAll((key, emitter) -> {
                SseMessage sseMessage = new SseMessage(key + "-" + messagePostfix);
                emitter.send(sseMessage, MediaType.APPLICATION_JSON);
            });
        }
    }
}
