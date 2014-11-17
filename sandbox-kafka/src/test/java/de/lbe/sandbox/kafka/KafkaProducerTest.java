package de.lbe.sandbox.kafka;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import kafka.admin.CreateTopicCommand;
import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;
import kafka.producer.KeyedMessage;
import kafka.producer.Producer;
import kafka.producer.ProducerConfig;
import kafka.server.KafkaServer;
import kafka.utils.TestUtils;

import org.I0Itec.zkclient.ZkClient;
import org.junit.Test;

import de.lbe.sandbox.kafka.consumer.ConsumerStarter;
import de.lbe.sandbox.kafka.consumer.MessageHandler;
import de.lbe.sandbox.kafka.embedded.EmbeddedKafka;
import de.lbe.sandbox.kafka.embedded.EmbeddedZookeeper;

public class KafkaProducerTest {

	private int brokerId;
	private String topic = "test";

	private ExecutorService executor;

	@Test
	public void producerTest() throws Exception {

		this.executor = Executors.newFixedThreadPool(5);

		// setup Zookeeper
		EmbeddedZookeeper zkServer = new EmbeddedZookeeper();
		zkServer.start();

		EmbeddedKafka kafka = new EmbeddedKafka(zkServer.getConnectionURL());
		kafka.start();

		// // setup Broker
		// int port = TestUtils.choosePort();
		// Properties props = TestUtils.createBrokerConfig(brokerId, port);
		// props.put("zookeeper.connect", zkServer.getConnectionURL());
		//
		// KafkaConfig config = new KafkaConfig(props);
		// Time mock = new MockTime();
		// KafkaServer kafkaServer = TestUtils.createServer(config, mock);

		// create topic
		ZkClient zkClient = zkServer.client();
		CreateTopicCommand.createTopic(zkClient, topic, 1, 1, "");

		List<KafkaServer> servers = new ArrayList<>();
		servers.add(kafka.getServer());
		TestUtils.waitUntilMetadataIsPropagated(scala.collection.JavaConversions.asScalaBuffer(servers), topic, 0, 5000);

		// setup producer
		Properties properties = TestUtils.getProducerConfig(kafka.getConnection(), SimplePartitioner.class.getName());

		ProducerConfig pConfig = new ProducerConfig(properties);
		Producer producer = new Producer(pConfig);

		// send message
		KeyedMessage<Integer, String> data = new KeyedMessage(topic, "test-message");

		List<KeyedMessage> messages = new ArrayList<KeyedMessage>();
		messages.add(data);

		producer.send(scala.collection.JavaConversions.asScalaBuffer(messages));

		final AtomicInteger consumerMessageCount = new AtomicInteger(0);
		MessageHandler messageHandler = new MessageHandler() {
			@Override
			public void handle(MessageAndMetadata<byte[], byte[]> message) {
				System.out.println("############## consuming");
				consumerMessageCount.incrementAndGet();
			}
		};
		startConsumers(messageHandler, 1, zkServer.getConnectionURL());

		Thread.sleep(1000);

		// cleanup
		producer.close();
		kafka.shutdown();
		zkClient.close();
		zkServer.shutdown();
	}

	/**
	 *
	 */
	private void startConsumers(MessageHandler messageHandler, int numberOfConsumers, String zxConnection) {
		Properties properties = new Properties();
		properties.put("zookeeper.connect", zxConnection);
		properties.put("group.id", "myGroupId");
		properties.put("zookeeper.session.timeout.ms", "4000");
		properties.put("zookeeper.sync.time.ms", "200");
		properties.put("auto.commit.interval.ms", "1000");
		ConsumerConfig config = new ConsumerConfig(properties);
		ConsumerConnector consumerConnector = Consumer.createJavaConsumerConnector(config);
		ConsumerStarter starter = new ConsumerStarter(consumerConnector, executor, messageHandler);
		starter.start(numberOfConsumers, this.topic);
	}

}