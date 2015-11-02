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

package de.lbe.sandbox.spring.websocket.client;

import java.nio.ByteBuffer;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.PingMessage;
import org.springframework.web.socket.PongMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class SimpleClientWebSocketHandler extends TextWebSocketHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(SimpleClientWebSocketHandler.class);

	private final CountDownLatch latch;

	private final AtomicReference<String> messagePayload;

	@Autowired
	public SimpleClientWebSocketHandler(CountDownLatch latch, AtomicReference<String> message) {
		this.latch = latch;
		this.messagePayload = message;
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		TextMessage message = new TextMessage(this.messagePayload.get());
		session.sendMessage(message);
		session.sendMessage(new PingMessage(ByteBuffer.wrap("sent from client".getBytes())));
	}

	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		LOGGER.info("Received: " + message + " (" + this.latch.getCount() + ")");
		this.messagePayload.set(message.getPayload());
		this.latch.countDown();
	}

	@Override
	protected void handlePongMessage(WebSocketSession session, PongMessage message) throws Exception {
		LOGGER.info("Recieved client side PONG message: {}", new String(message.getPayload().array()));
	}
}
