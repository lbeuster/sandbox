package de.lbe.sandbox.kafka.consumer;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

/**
 * @author lbeuster
 */
public class ConsumerStarter {

	private final ConsumerConnector consumerConnector;

	private final ExecutorService executor;

	private final MessageHandler messageHandler;

	/**
	 *
	 */
	public ConsumerStarter(ConsumerConnector consumerConnector, ExecutorService executor, MessageHandler messageHandler) {
		this.consumerConnector = consumerConnector;
		this.executor = executor;
		this.messageHandler = messageHandler;
	}

	/**
	 *
	 */
	public void start(int numberOfConsumers, String topicName) {

		// create streams for every thread
		Map<String, Integer> topicCountMap = Collections.singletonMap(topicName, numberOfConsumers);
		Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = this.consumerConnector.createMessageStreams(topicCountMap);
		List<KafkaStream<byte[], byte[]>> streams = consumerMap.get(topicName);

		// now create an object to consume the messages
		for (final KafkaStream<byte[], byte[]> stream : streams) {
			executor.submit(new MyConsumer(stream, this.messageHandler));
		}
	}
}