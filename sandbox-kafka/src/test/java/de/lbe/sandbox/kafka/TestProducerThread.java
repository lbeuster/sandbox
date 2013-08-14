package de.lbe.sandbox.kafka;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;

/**
 * https://cwiki.apache.org/confluence/display/KAFKA/0.8.0+Producer+Example
 */
public class TestProducerThread extends Thread {
	
	private Producer<String, String> producer;
	
	private volatile boolean stopped = false;
	
	/**
	 * 
	 */
	public TestProducerThread(Producer producer) {
		this.producer = producer;
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
