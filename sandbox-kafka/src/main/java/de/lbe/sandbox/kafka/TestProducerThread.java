package de.lbe.sandbox.kafka;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 */
public class TestProducerThread extends Thread {

	private static final Logger LOGGER = LoggerFactory.getLogger(TestProducerThread.class);

	private Producer<String, String> producer;

	private volatile boolean stopped = false;

	private final long sendIntervalInMillis;

	/**
	 * 
	 */
	public TestProducerThread(Producer<String, String> producer, long sendIntervalInMillis) {
		this.producer = producer;
		this.sendIntervalInMillis = sendIntervalInMillis;
	}

	/**
     *
     */
	@Override
	public void run() {
		int counter = 0;
		while (!this.stopped) {

			// create a message
			String partition = String.valueOf(System.currentTimeMillis());
			String message = "MessageBody-" + counter;
			KeyedMessage<String, String> data = new KeyedMessage<>(TestConfig.TOPIC_NAME, partition, message);

			// send
			System.out.println("sending message " + data);
			producer.send(data);

			counter++;
			if (this.sendIntervalInMillis > 0) {
				try {
					Thread.sleep(this.sendIntervalInMillis);
				} catch (InterruptedException e) {
					LOGGER.warn("Producer got interupt");
				}
			}
		}
		System.out.println("producer stopped");
	}

	/**
	 * 
	 */
	public void shutdown() {
		this.stopped = true;
	}
}
