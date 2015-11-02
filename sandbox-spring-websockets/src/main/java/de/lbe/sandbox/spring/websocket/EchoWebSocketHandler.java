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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.google.common.base.Throwables;

/**
 * Echo messages by implementing a Spring {@link WebSocketHandler} abstraction.
 */
public class EchoWebSocketHandler extends TextWebSocketHandler {

	private final OpenWebSockets openSessions;

	private static Logger logger = LoggerFactory.getLogger(EchoWebSocketHandler.class);

	public EchoWebSocketHandler(OpenWebSockets openSessions) {
		this.openSessions = openSessions;
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) {
		openSessions.add(session);
		logger.info("Opened new session in instance " + this);
	}

	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		String echoMessage = message.getPayload();
		logger.info("Recieving server side message: " + echoMessage);
		openSessions.doForAll((id, webSession) -> {
			try {
				webSession.sendMessage(new TextMessage(echoMessage));
			} catch (Exception ex) {
				throw Throwables.propagate(ex);
			}
		});
	}

	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		session.close(CloseStatus.SERVER_ERROR);
	}

}
