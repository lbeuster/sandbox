package de.lbe.sandbox.kafka;

import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import kafka.admin.CreateTopicCommand;
import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.javaapi.producer.Producer;
import kafka.message.MessageAndMetadata;
import kafka.producer.DefaultPartitioner;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import kafka.serializer.StringEncoder;

import org.I0Itec.zkclient.ZkClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.lbe.sandbox.kafka.consumer.ConsumerStarter;
import de.lbe.sandbox.kafka.consumer.MessageHandler;
import de.lbe.sandbox.kafka.embedded.OldEmbeddedKafkaCluster;

public class EmbeddedKafkaTest {

	private static final String TOPIC = "testTopic";

	private static final String CONSUMER_GROUP = "testConsumerGroup";

	private ExecutorService executor;

	private OldEmbeddedKafkaCluster embeddedKafkaCluster;

	/**
	 *
	 */
	@Before
	public void setUp() throws Exception {
		this.executor = Executors.newFixedThreadPool(5);
		this.embeddedKafkaCluster = new OldEmbeddedKafkaCluster();
		this.embeddedKafkaCluster.start();
		// create topic
		ZkClient zkClient = new ZkClient(embeddedKafkaCluster.getZkConnection());
		CreateTopicCommand.createTopic(zkClient, TOPIC, 1, 1, "");
	}

	/**
	 *
	 */
	@After
	public void tearDown() {
		this.embeddedKafkaCluster.stop();
		this.executor.shutdown();
	}

	@Test
	public void testEmbedded() throws Exception {

		// start consumers
		final AtomicInteger consumerMessageCount = new AtomicInteger(0);
		MessageHandler messageHandler = new MessageHandler() {
			@Override
			public void handle(MessageAndMetadata<byte[], byte[]> message) {
				consumerMessageCount.incrementAndGet();
			}
		};
		startConsumers(messageHandler, 3);

		// start producer
		final Producer<String, String> producer = createProducer();
		final int producerMessageCount = 5;
		Thread producerThread = new Thread() {
			@Override
			public void run() {
				for (int i = 0; i < producerMessageCount; i++) {
					String partition = String.valueOf(System.currentTimeMillis());
					String message = "MessageBody-" + consumerMessageCount;
					KeyedMessage<String, String> data = new KeyedMessage<>(TOPIC, partition, message);
					producer.send(data);
				}
			}
		};
		producerThread.start();
		producerThread.join();

		System.out.println(consumerMessageCount);

		System.out.println("### Embedded Kafka cluster broker list: " + embeddedKafkaCluster.getBrokerList());
	}

	/**
	 *
	 */
	private void startConsumers(MessageHandler messageHandler, int numberOfConsumers) {
		Properties properties = new Properties();
		properties.putAll(this.embeddedKafkaCluster.getConsumerProperties());
		properties.put("group.id", CONSUMER_GROUP);
		properties.put("zookeeper.session.timeout.ms", "4000");
		properties.put("zookeeper.sync.time.ms", "200");
		properties.put("auto.commit.interval.ms", "1000");
		ConsumerConfig config = new ConsumerConfig(properties);
		ConsumerConnector consumerConnector = Consumer.createJavaConsumerConnector(config);
		ConsumerStarter starter = new ConsumerStarter(consumerConnector, executor, messageHandler);
		starter.start(numberOfConsumers, TOPIC);
	}

	/**
	 *
	 */
	private Producer<String, String> createProducer() {
		Properties properties = new Properties();
		properties.putAll(this.embeddedKafkaCluster.getProducerProperties());
		properties.put("serializer.class", StringEncoder.class.getName());
		properties.put("partitioner.class", DefaultPartitioner.class.getName());
		properties.put("request.required.acks", "1");
		ProducerConfig config = new ProducerConfig(properties);
		Producer<String, String> producer = new Producer<>(config);
		return producer;
	}
}
