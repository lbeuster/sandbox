package de.lbe.sandbox.kafka;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;

import de.lbe.sandbox.kafka.consumer.ConsumerStarter;
import de.lbe.sandbox.kafka.consumer.MessageHandler;

public class TestConsumerStarter {

	private ConsumerConnector consumerConnector;

	private ExecutorService executor;

	/**
	 *
	 */
	TestConsumerStarter(ConsumerConnector consumerConnector, int numberOfThreads) {
		this.consumerConnector = consumerConnector;
		executor = Executors.newFixedThreadPool(numberOfThreads);
	}

	public void stop() {
		if (executor != null) {
			executor.shutdown();
		}
	}

	/**
	 *
	 */
	public void start(int numberOfConsumers) {
		MessageHandler messageHandler = new MessageHandler() {

			@Override
			public void handle(MessageAndMetadata<byte[], byte[]> message) {
				String messageBody = new String(message.message());
				System.out.println("consuming(" + Thread.currentThread().getName() + "): " + messageBody);
			}
		};
		ConsumerStarter starter = new ConsumerStarter(consumerConnector, executor, messageHandler);
		starter.start(numberOfConsumers, TestConfig.TOPIC_NAME);
	}
}