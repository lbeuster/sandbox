package de.lbe.sandbox.kafka;

import java.util.Properties;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.javaapi.producer.Producer;
import kafka.producer.ProducerConfig;
import kafka.serializer.StringEncoder;

/**
 * https://cwiki.apache.org/confluence/display/KAFKA/0.8.0+Producer+Example https://cwiki.apache.org/confluence/display/KAFKA/Consumer+Group+Example
 * 
 * (1) Start zookeper (2) Start kafka (3) create queue: bin/kafka-create-topic.sh --topic TestProducer --replica 1 --zookeeper localhost:2181
 * --partition 5
 */
public class Main {

	/**
	 * 
	 */
	public static final void main(String[] args) throws Throwable {
		Producer<String, String> producer = null;
		TestProducerThread producerThread = null;
		ConsumerConnector consumer = null;
		TestConsumerStarter consumerStarter = null;
		try {

			// producer
			producer = createProducer();
			producerThread = new TestProducerThread(producer, 500);

			// consumer
			consumer = createConsumer();
			consumerStarter = new TestConsumerStarter(consumer);

			// start: our topic has 5 partitions - so we need at max 5 consumers
			producerThread.start();
			consumerStarter.start(5);

			Thread.sleep(10_000);
		} catch (Throwable ex) {
			ex.printStackTrace();
			throw ex;
		} finally {
			if (producerThread != null) {
				producerThread.shutdown();
				producerThread.join();
			}
			if (producer != null) {
				producer.close();
			}
			if (consumerStarter != null) {
				consumerStarter.stop();
			}
			if (consumer != null) {
				consumer.shutdown();
			}
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
