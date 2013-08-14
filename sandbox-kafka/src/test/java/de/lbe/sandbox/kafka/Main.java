package de.lbe.sandbox.kafka;

import java.util.Properties;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.javaapi.producer.Producer;
import kafka.producer.ProducerConfig;
import kafka.serializer.StringEncoder;

import org.junit.Assert;

/**
 * https://cwiki.apache.org/confluence/display/KAFKA/0.8.0+Producer+Example
 */
public class Main extends Assert {

	/**
	 * 
	 */
	public static final void main(String[] args) throws Exception {
		Producer<String, String> producer = null;
		TestProducerThread producerThread = null;
		ConsumerConnector consumer = null;
		TestConsumerStarter consumerStarter = null;
		try {

			// producer
			producer = createProducer();
			producerThread = new TestProducerThread(producer);

			// consumer
			consumer = createConsumer();
			consumerStarter = new TestConsumerStarter(consumer);

			// start
			producerThread.start();
			consumerStarter.start(4);

			Thread.sleep(2000);
		} finally {
			producerThread.shutdown();
			producerThread.join();
			producer.close();
			consumerStarter.stop();
			consumer.shutdown();
		}
	}

	/**
     *
     */
	public static Producer<String, String> createProducer() {

		Properties properties = new Properties();
		properties.put("metadata.broker.list", TestConfig.BROKERS);
		properties.put("serializer.class", StringEncoder.class.getName());
		properties.put("partitioner.class", SimplePartitioner.class.getName());
		properties.put("request.required.acks", "1");

		ProducerConfig config = new ProducerConfig(properties);

		Producer<String, String> producer = new Producer<>(config);
		return producer;
	}

	/**
	 * 
	 */
	public static ConsumerConnector createConsumer() {

		Properties props = new Properties();
		props.put("zookeeper.connect", TestConfig.ZOOKEEPER);
		props.put("group.id", TestConfig.CONSUMER_GROUP);
		props.put("zookeeper.session.timeout.ms", "400");
		props.put("zookeeper.sync.time.ms", "200");
		props.put("auto.commit.interval.ms", "1000");

		ConsumerConfig config = new ConsumerConfig(props);
		ConsumerConnector consumer = Consumer.createJavaConsumerConnector(config);
		return consumer;
	}

}
