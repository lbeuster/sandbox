package de.lbe.sandbox.kafka;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import kafka.admin.CreateTopicCommand;
import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.javaapi.producer.Producer;
import kafka.message.MessageAndMetadata;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import kafka.serializer.StringDecoder;
import kafka.serializer.StringEncoder;
import kafka.server.KafkaConfig;
import kafka.server.KafkaServer;
import kafka.utils.MockTime;
import kafka.utils.TestUtils;
import kafka.utils.TestZKUtils;
import kafka.utils.Time;
import kafka.utils.ZKStringSerializer$;
import kafka.zk.EmbeddedZookeeper;

import org.I0Itec.zkclient.ZkClient;
import org.junit.Test;

public class OriginalKafkaProducerTest {

	private int brokerId = 0;
	private String topic = "test";

	@Test
	public void producerTest() throws Exception {

		// setup Zookeeper
		String zkConnect = TestZKUtils.zookeeperConnect();
		EmbeddedZookeeper zkServer = new EmbeddedZookeeper(zkConnect);
		ZkClient zkClient = new ZkClient(zkServer.connectString(), 30000, 30000, ZKStringSerializer$.MODULE$);

		// setup Broker
		int port = TestUtils.choosePort();
		Properties props = TestUtils.createBrokerConfig(brokerId, port);

		KafkaConfig config = new KafkaConfig(props);
		Time mock = new MockTime();
		KafkaServer kafkaServer = TestUtils.createServer(config, mock);

		// create topic
		CreateTopicCommand.createTopic(zkClient, topic, 1, 1, "");

		List<KafkaServer> servers = new ArrayList<KafkaServer>();
		servers.add(kafkaServer);
		TestUtils.waitUntilMetadataIsPropagated(scala.collection.JavaConversions.asScalaBuffer(servers), topic, 0, 5000);

		// setup producer
		Producer producer = null;
		// producer = createProducer(port);
		kafka.producer.Producer otherProducer = createOtherProducer(port);

		Thread consumer = startConsumer(1, zkConnect);

		// send message
		// sendTestMessage(producer);
		sendTestMessage(otherProducer);

		consumer.join(2000);

		// cleanup
		if (producer != null) {
			producer.close();
		}
		kafkaServer.shutdown();
		zkClient.close();
		zkServer.shutdown();
	}

	/**
	 *
	 */
	private Thread startConsumer(int numberOfConsumers, String zxConnection) throws Exception {

		Properties properties = new Properties();
		properties.put("zookeeper.connect", zxConnection);
		properties.put("group.id", "myGroupId");
		properties.put("zookeeper.session.timeout.ms", "4000");
		properties.put("zookeeper.sync.time.ms", "200");
		properties.put("auto.commit.interval.ms", "1000");
		ConsumerConfig config = new ConsumerConfig(properties);
		ConsumerConnector consumerConnector = Consumer.createJavaConsumerConnector(config);

		// create streams for every thread
		Map<String, Integer> topicCountMap = Collections.singletonMap(topic, 1);
		Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumerConnector.createMessageStreams(topicCountMap);
		List<KafkaStream<byte[], byte[]>> streams = consumerMap.get(topic);

		final KafkaStream<byte[], byte[]> stream = streams.get(0);
		Thread thread = new Thread() {
			@Override
			public void run() {
				ConsumerIterator<byte[], byte[]> it = stream.iterator();
				StringDecoder decoder = new StringDecoder(null);
				while (it.hasNext()) {
					MessageAndMetadata<byte[], byte[]> message = it.next();
					System.out.println("------" + decoder.fromBytes(message.message()));
				}
			}
		};
		thread.start();
		return thread;
	}

	private Producer createProducer(int port) {
		Properties properties = TestUtils.getProducerConfig("localhost:" + port, "kafka.producer.DefaultPartitioner");
		properties.put("serializer.class", StringEncoder.class.getName());
		// properties.put("partitioner.class", SimplePartitioner.class.getName());
		ProducerConfig producerConfig = new ProducerConfig(properties);
		Producer<String, String> producer = new Producer<>(producerConfig);
		return producer;
	}

	private void sendTestMessage(Producer producer) {
		String partition = String.valueOf(System.currentTimeMillis());
		String message = "MessageBody-1";
		KeyedMessage<String, String> data = new KeyedMessage<>(topic, partition, message);
		producer.send(data);
	}

	private kafka.producer.Producer createOtherProducer(int port) {
		Properties properties = TestUtils.getProducerConfig("localhost:" + port, "kafka.producer.DefaultPartitioner");
		ProducerConfig pConfig = new ProducerConfig(properties);
		kafka.producer.Producer producer = new kafka.producer.Producer(pConfig);
		return producer;
	}

	private void sendTestMessage(kafka.producer.Producer producer) {
		// send message
		KeyedMessage<Integer, String> data = new KeyedMessage(topic, "test-message");
		List<KeyedMessage> messages = new ArrayList<KeyedMessage>();
		messages.add(data);
		producer.send(scala.collection.JavaConversions.asScalaBuffer(messages));
	}
}