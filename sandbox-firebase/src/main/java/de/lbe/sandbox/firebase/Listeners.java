package de.lbe.sandbox.firebase;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * @author lbeuster
 */
class Listeners {

	static <T> void accept(Consumer<T> consumer, T value) {
		if (consumer != null) {
			consumer.accept(value);
		}
	}

	static <T, R> void accept(BiConsumer<T, R> consumer, T value, R result) {
		if (consumer != null) {
			consumer.accept(value, result);
		}
	}
}
