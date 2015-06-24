package de.lbe.sandbox.spring.amqp;

import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.inject.Inject;

import org.junit.Test;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Binding.DestinationType;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.MessagePropertiesBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;

import com.rabbitmq.client.ConnectionFactory;

import de.asideas.ipool.commons.lib.spring.test.AbstractSpringIT;
import de.asideas.ipool.commons.lib.util.logging.mdc.RequestId;

/**
 * @author lbeuster
 */
@ContextConfiguration(classes = SimpleAnnotationTest.TestConfiguration.class)
public class SimpleAnnotationTest extends AbstractSpringIT {

	public static final String RABBIT_URL = "amqp://ypecsgox:bSsjVb9EnQdBuo5ZJvyqdTdlzoQ_IzoR@bunny.cloudamqp.com/ypecsgox";

	private static final String QUEUE = "larsq";
	private static final String EXCHANGE = "larsx";
	private static final String ROUTING_KEY = "larsr";

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
		MessageProperties properities = MessagePropertiesBuilder.newInstance().setContentType("text/plain").setCorrelationId(requestId.getBytes()).build();
		for (int i = 0; i < 10; i++) {
			template.send(EXCHANGE, ROUTING_KEY, new Message("CONTENT".getBytes(), properities));
		}
		Thread.sleep(5000);
	}

	/**
	 *
	 */
	@Configuration
	@EnableRabbit
	@Import({ MyListener.class })
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
			admin.declareQueue(new Queue(QUEUE));
			admin.declareBinding(new Binding(QUEUE, DestinationType.QUEUE, EXCHANGE, ROUTING_KEY, null));
			return admin;
		}

		/**
		 * This singleton is needed by the rabbit infrastructure with exactly this name:
		 * RabbitListenerAnnotationBeanPostProcessor.DEFAULT_RABBIT_LISTENER_CONTAINER_FACTORY_BEAN_NAME
		 */
		@Bean
		public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(CachingConnectionFactory connectionFactory) {
			SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
			factory.setConnectionFactory(connectionFactory);
			factory.setConcurrentConsumers(3);
			factory.setMaxConcurrentConsumers(10);
			// factory.setAcknowledgeMode(AcknowledgeMode.AUTO);
			return factory;
		}
	}

	/**
	 *
	 */
	@Component
	static class MyListener {

		@RabbitListener(queues = QUEUE)
		public void handle(Message message) {
			System.out.println("received: " + message);
		}
	}
}