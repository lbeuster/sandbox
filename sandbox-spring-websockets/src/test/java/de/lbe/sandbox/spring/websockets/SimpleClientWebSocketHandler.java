package de.lbe.sandbox.spring.websockets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class SimpleClientWebSocketHandler extends TextWebSocketHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(SimpleClientWebSocketHandler.class);

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		TextMessage message = new TextMessage("Hello");
		session.sendMessage(message);
	}

	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		LOGGER.info("Received: {}", message);
		session.close();
	}
}