package de.lbe.sandbox.spring.amqp;

import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.inject.Inject;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Binding.DestinationType;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.MessagePropertiesBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

import com.rabbitmq.client.ConnectionFactory;

import de.asideas.ipool.commons.lib.spring.test.AbstractSpringIT;
import de.asideas.ipool.commons.lib.util.logging.mdc.RequestId;

/**
 * @author lbeuster
 */
@ContextConfiguration(classes = ManualTest.TestConfiguration.class)
public class ManualTest extends AbstractSpringIT {

	private static final Logger LOGGER = LoggerFactory.getLogger(ManualTest.class);

	public static final String RABBIT_URL = "amqp://ypecsgox:bSsjVb9EnQdBuo5ZJvyqdTdlzoQ_IzoR@bunny.cloudamqp.com/ypecsgox";

	private static final String EXCHANGE = "exchange";
	private static final String ROUTING_KEY1 = "routing.1";
	private static final String ROUTING_KEY2 = "routing.2";
	private static final String QUEUE1 = "queue1";
	private static final String QUEUE2 = "queue2";

	@Inject
	private RabbitTemplate template;

	@Test
	public void test() throws Exception {

		// SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		// container.setConnectionFactory(connectionFactory);
		// container.setQueueNames(queue);
		// container.setMessageListener(new MessageListener() {
		// @Override
		// public void onMessage(Message message) {
		// System.out.println("received: " + message);
		// }
		// });
		// container.start();

		String requestId = RequestId.getOrInit();
		MessageProperties properties = MessagePropertiesBuilder.newInstance().setContentType("text/plain").setCorrelationId(requestId.getBytes()).build();
		for (int i = 0; i < 10; i++) {
			template.send(EXCHANGE, ROUTING_KEY1, new Message("CONTENT1".getBytes(), properties));
			template.send(EXCHANGE, ROUTING_KEY2, new Message("CONTENT2".getBytes(), properties));
		}
		Thread.sleep(5000);
	}

	/**
	 *
	 */
	@Configuration
	static class TestConfiguration {

		@Bean
		CachingConnectionFactory rabbitConnectionFactory() throws KeyManagementException, NoSuchAlgorithmException, URISyntaxException {
			ConnectionFactory rabbitFactory = new ConnectionFactory();
			rabbitFactory.setUri(RABBIT_URL);
			CachingConnectionFactory factory = new CachingConnectionFactory(rabbitFactory);
			return factory;
		}

		@Bean
		RabbitTemplate rabbitTemplate(CachingConnectionFactory connectionFactory) {
			return new RabbitTemplate(connectionFactory);
		}

		@Bean
		RabbitAdmin rabbitAdmin(CachingConnectionFactory connectionFactory) {
			RabbitAdmin admin = new RabbitAdmin(connectionFactory);
			admin.declareExchange(new TopicExchange(EXCHANGE));
			admin.declareQueue(new Queue(QUEUE1));
			admin.declareQueue(new Queue(QUEUE2));
			admin.declareBinding(new Binding(QUEUE1, DestinationType.QUEUE, EXCHANGE, ROUTING_KEY1, null));
			admin.declareBinding(new Binding(QUEUE2, DestinationType.QUEUE, EXCHANGE, ROUTING_KEY2, null));
			return admin;
		}

		@Bean
		public MessageListenerContainer consumerConsumer1(CachingConnectionFactory connectionFactory) {
			SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
			container.setConnectionFactory(connectionFactory);
			container.addQueueNames(QUEUE1);
			container.setMessageListener(new TestMessageListener());
			container.setConcurrentConsumers(2);
			return container;
		}

		@Bean
		public MessageListenerContainer consumerConsumer2(CachingConnectionFactory connectionFactory) {
			SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
			container.setConnectionFactory(connectionFactory);
			container.addQueueNames(QUEUE2);
			container.setMessageListener(new TestMessageListener());
			container.setConcurrentConsumers(3);
			return container;
		}

	}

	/**
	 *
	 */
	static class TestMessageListener implements MessageListener {

		@Override
		public void onMessage(Message message) {
			LOGGER.info("received: {}", message);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
			LOGGER.info("done: {}", message);
		}
	}
}