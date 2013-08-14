package de.lbe.sandbox.kafka;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;

public class TestConsumerStarter {

	private ConsumerConnector consumerConnector;

	/**
	 * 
	 */
	TestConsumerStarter(ConsumerConnector consumerConnector) {
		this.consumerConnector = consumerConnector;
	}

	private ExecutorService executor;

	public void stop() {
		if (executor != null) {
			executor.shutdown();
		}
	}

	/**
	 * 
	 */
	public void start(int numberOfThreads) {

		// create streams for every thread
		Map<String, Integer> topicCountMap = Collections.singletonMap(TestConfig.TOPIC_NAME, numberOfThreads);
		Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = this.consumerConnector.createMessageStreams(topicCountMap);
		List<KafkaStream<byte[], byte[]>> streams = consumerMap.get(TestConfig.TOPIC_NAME);

		// now launch all the threads
		executor = Executors.newFixedThreadPool(numberOfThreads);

		// now create an object to consume the messages
		int threadNumber = 0;
		for (final KafkaStream<byte[], byte[]> stream : streams) {
			executor.submit(new TestConsumer(stream, threadNumber));
			threadNumber++;
		}
	}

	/**
	 * 
	 */
	public class TestConsumer implements Runnable {

		private KafkaStream<byte[], byte[]> stream;
		private int threadNumber;

		public TestConsumer(KafkaStream<byte[], byte[]> stream, int threadNumber) {
			this.threadNumber = threadNumber;
			this.stream = stream;
		}

		public void run() {
			ConsumerIterator<byte[], byte[]> it = stream.iterator();
			while (it.hasNext()) {
				MessageAndMetadata<byte[], byte[]> message = it.next();
				String messageBody = new String(message.message());
				System.out.println("consuming(" + threadNumber + "): " + messageBody);
			}
			System.out.println("Shutting down consumer thread: " + threadNumber);
		}
	}
}