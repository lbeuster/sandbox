package de.lbe.sandbox.firebase;

import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lbeuster
 */
class ThrowableLoggingConsumer<T> implements Consumer<T> {

	private static final Logger LOGGER = LoggerFactory.getLogger(ThrowableLoggingConsumer.class);

	private final Consumer<T> consumer;

	ThrowableLoggingConsumer(Consumer<T> consumer) {
		this.consumer = consumer;
	}

	@Override
	public void accept(T value) {
		try {
			consumer.accept(value);
		} catch (Exception ex) {
			LOGGER.error("Caught exception", ex);
		}
	}
}
