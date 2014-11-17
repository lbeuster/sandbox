package de.lbe.sandbox.kafka.consumer;

import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.message.MessageAndMetadata;

/**
 * @author lbeuster
 */
public class MyConsumer implements Runnable {

	private final KafkaStream<byte[], byte[]> stream;

	private final MessageHandler messageHandler;

	public MyConsumer(KafkaStream<byte[], byte[]> stream, MessageHandler messageHandler) {
		this.stream = stream;
		this.messageHandler = messageHandler;
	}

	@Override
	public void run() {
		ConsumerIterator<byte[], byte[]> it = stream.iterator();
		while (it.hasNext()) {
			MessageAndMetadata<byte[], byte[]> message = it.next();
			handle(message);
		}
	}

	protected void handle(MessageAndMetadata<byte[], byte[]> message) {
		this.messageHandler.handle(message);
	}
}