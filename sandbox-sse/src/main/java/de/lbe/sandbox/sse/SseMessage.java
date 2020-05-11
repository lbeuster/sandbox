package de.lbe.sandbox.sse;

import com.cartelsol.commons.lib.lang.ToString;

/**
 * @author lbeuster
 */
public class SseMessage {

    private final String message;

    public SseMessage(String message) {
        this.message = message;
    }

    @SuppressWarnings("unused")
    private SseMessage() {
        // needed by Jackson
        this(null);
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return ToString.toString(this);
    }
}
