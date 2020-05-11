package de.lbe.sandbox.sse;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.PreDestroy;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import com.cartelsol.commons.lib.lang.MoreThrowables;
import com.cartelsol.commons.lib.lang.ThrowingRunnable;
import com.cartelsol.commons.lib.util.function.ThrowingBiConsumer;

/**
 * @author lbeuster
 */
@Component
public class SseConnectionManager {

    private static final Logger LOGGER = SseLogger.LOGGER;

    private final Map<String, SseConnection<?>> connections = new ConcurrentHashMap<>();

    private final ThreadPoolExecutor executor;

    private final AtomicBoolean shutdownInProgess = new AtomicBoolean(false);

    public SseConnectionManager() {
        this.executor = createExecutor(5);
    }

    /**
     *
     */
    @PreDestroy
    public void stop() {
        this.shutdownInProgess.set(true);
        closeConnections();
        executor.shutdown();
    }

    /**
     *
     */
    public <T> SseConnection<T> newConnection(String key, T params) {
        SseConnection<T> connection = new SseConnection<>(params, TimeUnit.SECONDS.toMillis(10));
        connection.onCompletion(() -> {
            LOGGER.info("Completed SSE connection for key={}", key);
            closeConnection(key);
        });
        connection.onTimeout(() -> {
            LOGGER.info("Closing SSE connection for key={} because of timeout", key);
            closeConnection(key);
        });
        add(key, connection);
        return connection;
    }

    /**
     *
     */
    @SuppressWarnings("boxing")
    public <T> void scheduleForAll(ThrowingBiConsumer<String, SseConnection<T>, ?> consumer) {
        LOGGER.info("Applying function to {} connections: {}", numberOfConnections(), consumer);
        this.connections.entrySet().forEach(entry -> {
            String key = entry.getKey();
            SseConnection<T> connection = SseConnection.class.cast(entry.getValue());
            schedule(key, () -> consumer.accept(key, connection), false);
        });
    }

    /**
     *
     */
    public void schedule(String key, ThrowingRunnable<?> action, boolean force) {
        executor.execute(() -> {
            try {

                // an additional check to prevent already scheduled tasks to be executed in case the have been removed in the meantime
                if (!force && !connections.containsKey(key)) {
                    return;
                }

                // really run
                action.run();
            } catch (Throwable ex) { // NOSONAR: Catch Exception instead of Throwable.
                LOGGER.error("Caught exception while executing SSE consumer={}, closing connection", key, ex);
                closeConnection(key);
                MoreThrowables.rethrowThreadDeath(ex);
            }
        });
    }

    public <T> SseConnection<T> get(String key) {
        return SseConnection.class.cast(this.connections.get(key));
    }

    private void add(String key, SseConnection<?> connection) {
        LOGGER.info("adding SSE connection for key={}", key);
        SseConnection<?> old = this.connections.put(key, connection);
        if (old != null) {
            LOGGER.warn("SSE connection for key={} already exists, overwriting with new one", key);
        }
    }

    public void closeConnections() {
        scheduleForAll((key, connection) -> connection.complete());
        this.connections.clear();
    }

    private void closeConnection(String key) {
        this.connections.remove(key);
    }

    public int numberOfConnections() {
        return connections.size();
    }

    /**
     *
     */
    private ThreadPoolExecutor createExecutor(int maxThreads) {
        ThreadFactory threadFactory = new BasicThreadFactory.Builder().daemon(true).namingPattern("SseExecutor-%s").build();
        BlockingQueue<Runnable> queue = new LinkedBlockingQueue<>(1_000);
        ThreadPoolExecutor executor = new ThreadPoolExecutor(maxThreads, maxThreads, 1, TimeUnit.MINUTES, queue, threadFactory);
        executor.setRejectedExecutionHandler((runnable, exe) -> LOGGER.error("Rejecting SSE execution because the queue is full"));
        return executor;
    }
}
