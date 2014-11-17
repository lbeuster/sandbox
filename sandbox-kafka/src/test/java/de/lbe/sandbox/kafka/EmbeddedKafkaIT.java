package de.lbe.sandbox.kafka;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import kafka.javaapi.consumer.ConsumerConnector;
import kafka.javaapi.producer.Producer;
import kafka.message.MessageAndMetadata;
import kafka.producer.KeyedMessage;
import kafka.serializer.StringEncoder;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.asideas.ipool.commons.kafka.test.EmbeddedKafka;
import de.asideas.ipool.commons.kafka.test.EmbeddedZookeeper;
import de.asideas.lib.commons.lang.XRunnable;
import de.asideas.lib.commons.test.junit.AbstractJUnitTest;
import de.lbe.sandbox.kafka.consumer.ConsumerStarter;
import de.lbe.sandbox.kafka.consumer.MessageHandler;

/**
 * @author lbeuster
 */
public class EmbeddedKafkaIT extends AbstractJUnitTest {

	private static final String TOPIC = "TEST_TOPIC";

	private ExecutorService executor;

	private EmbeddedZookeeper zk;

	private EmbeddedKafka kafka;

	/**
	 *
	 */
	@Before
	public void setUp() {
		this.zk = new EmbeddedZookeeper(2182);
		this.zk.start();

		this.kafka = new EmbeddedKafka(this.zk);
		this.kafka.start();
		this.kafka.createTopic(TOPIC);

		this.executor = Executors.newFixedThreadPool(5);
	}

	/**
	 *
	 */
	@After
	public void tearDown() {
		kafka.shutdown();
		zk.shutdown();
	}

	/**
	 *
	 */
	@Test
	public void producerTest() throws Exception {

		// producer
		Producer<String, String> producer = kafka.createProducer(StringEncoder.class);

		// consumer
		ConsumerConnector consumerConnector = kafka.createConsumerConnector("MYGROUPID");
		final AtomicInteger numConsumedMessages = new AtomicInteger(0);
		MessageHandler messageHandler = new MessageHandler() {
			@Override
			public void handle(MessageAndMetadata<byte[], byte[]> message) {
				numConsumedMessages.incrementAndGet();
			}
		};
		ConsumerStarter starter = new ConsumerStarter(consumerConnector, executor, messageHandler);
		starter.start(1, TOPIC);

		// send message
		final int numProducedMessages = 10;
		for (int i = 0; i < numProducedMessages; i++) {
			String partition = String.valueOf(System.currentTimeMillis());
			String message = "MessageBody-" + i;
			KeyedMessage<String, String> data = new KeyedMessage<>(TOPIC, partition, message);
			producer.send(data);
		}

		// wait until the messages have been consumed
		assertWithinTime(new XRunnable<Void>() {
			@Override
			protected void runImpl() {
				assertEquals(numProducedMessages, numConsumedMessages.get());
			}
		}, 5_000);

		// shutdown
		consumerConnector.shutdown();

		// cleanup
		producer.close();
	}
}