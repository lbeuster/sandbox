package de.lbe.sandbox.sse;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.cartelsol.commons.lib.lang.RunnableList;

/**
 * @author lbeuster
 */
public class SseConnection<T> extends SseEmitter {

    private final T attributes;

    private final RunnableList onCompletion;

    private final RunnableList onTimeout;

    @SuppressWarnings("boxing")
    public SseConnection(T attributes, long timeoutInMillis) {
        super(timeoutInMillis);
        this.attributes = attributes;
        this.onCompletion = RunnableList.empty();
        this.onTimeout = RunnableList.empty();
        super.onCompletion(this.onCompletion);
        super.onTimeout(this.onTimeout);
    }

    public T getAttributes() {
        return attributes;
    }

    @Override
    public synchronized void onTimeout(Runnable callback) {
        this.onTimeout.add(callback);
    }

    @Override
    public synchronized void onCompletion(Runnable callback) {
        this.onCompletion.add(callback);
    }
}