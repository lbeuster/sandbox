/*
 * Copyright 2012-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.lbe.sandbox.spring.websocket;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.PropertyPlaceholderAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import de.lbe.sandbox.spring.websocket.client.SimpleClientWebSocketHandler;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SampleTomcatWebSocketApplication.class)
@WebIntegrationTest
@DirtiesContext
public class SampleWebSocketsApplicationTests {

	private static Log logger = LogFactory.getLog(SampleWebSocketsApplicationTests.class);

	@Value("${local.server.port}")
	private int port = 1234;

	@Test
	public void echoEndpoint() throws Exception {
		ConfigurableApplicationContext context = new SpringApplicationBuilder(ClientConfiguration.class, PropertyPlaceholderAutoConfiguration.class)
			.properties("websocket.uri:ws://localhost:" + this.port + "/echo/websocket").run("--spring.main.web_environment=false");
		long count = context.getBean(ClientConfiguration.class).latch.getCount();
		AtomicReference<String> messagePayloadReference = context.getBean(ClientConfiguration.class).messagePayload;
		context.close();
		assertEquals(0, count);
		assertEquals("Greeting", messagePayloadReference.get());
	}

	@Test
	public void reverseEndpoint() throws Exception {
		ConfigurableApplicationContext context = new SpringApplicationBuilder(ClientConfiguration.class, PropertyPlaceholderAutoConfiguration.class)
			.properties("websocket.uri:ws://localhost:" + this.port + "/reverse").run("--spring.main.web_environment=false");
		long count = context.getBean(ClientConfiguration.class).latch.getCount();
		AtomicReference<String> messagePayloadReference = context.getBean(ClientConfiguration.class).messagePayload;
		context.close();
		assertEquals(0, count);
		assertEquals("Reversed: gniteerG", messagePayloadReference.get());
	}

	@Test
	public void testLars() throws Exception {

		for (int i = 0; i < 1; i++) {
			StandardWebSocketClient client = new StandardWebSocketClient();
			SimpleClientWebSocketHandler handler = new SimpleClientWebSocketHandler(new CountDownLatch(1), new AtomicReference<>("Greeting" + i));

			WebSocketConnectionManager manager = new WebSocketConnectionManager(client, handler, "ws://localhost:" + this.port + "/echo/websocket");
			manager.start();
		}
		System.out.println("hallo");

	}

	@Configuration
	static class ClientConfiguration implements CommandLineRunner {

		@Value("${websocket.uri}")
		private String webSocketUri;

		private final CountDownLatch latch = new CountDownLatch(1);

		private final AtomicReference<String> messagePayload = new AtomicReference<String>();

		@Override
		public void run(String... args) throws Exception {
			logger.info("Waiting for response: latch=" + this.latch.getCount());
			if (this.latch.await(10, TimeUnit.SECONDS)) {
				logger.info("Got response: " + this.messagePayload.get());
			} else {
				logger.info("Response not received: latch=" + this.latch.getCount());
			}
		}

		@Bean
		public WebSocketConnectionManager wsConnectionManager() {

			WebSocketConnectionManager manager = new WebSocketConnectionManager(client(), handler(), this.webSocketUri);
			manager.setAutoStartup(true);

			return manager;
		}

		public StandardWebSocketClient client() {
			return new StandardWebSocketClient();
		}

		public SimpleClientWebSocketHandler handler() {
			return new SimpleClientWebSocketHandler(this.latch, this.messagePayload);
		}
	}

}
