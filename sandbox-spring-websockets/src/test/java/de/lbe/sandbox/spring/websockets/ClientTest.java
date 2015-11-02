package de.lbe.sandbox.spring.websockets;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.RestTemplateXhrTransport;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

/**
 * @author lbeuster
 */
public class ClientTest extends AbstractWebsocketTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(ClientTest.class);

	/**
	 *
	 */
	@Test
	public void testClient() throws Exception {

		StompSessionHandler handler = new AbstractTestSessionHandler() {

			@Override
			public void afterConnected(final StompSession session, StompHeaders connectedHeaders) {
				session.subscribe("/topic/greeting", new StompFrameHandler() {
					@Override
					public Type getPayloadType(StompHeaders headers) {
						return Greeting.class;
					}

					@Override
					public void handleFrame(StompHeaders headers, Object payload) {
						System.out.println(payload);
					}
				});
				System.out.println("afterConnected");
				session.send("/hello", new HelloMessage());
			}
		};

		WebSocketHttpHeaders headers = new WebSocketHttpHeaders();

		List<Transport> transports = new ArrayList<>();
		transports.add(new WebSocketTransport(new StandardWebSocketClient()));
		RestTemplateXhrTransport xhrTransport = new RestTemplateXhrTransport(new RestTemplate());
		xhrTransport.setRequestHeaders(headers);
		transports.add(xhrTransport);

		SockJsClient sockJsClient = new SockJsClient(transports);

		WebSocketStompClient stompClient = new WebSocketStompClient(sockJsClient);
		stompClient.setMessageConverter(new MappingJackson2MessageConverter());
		stompClient.connect("ws://localhost:{port}/hello", headers, handler, 8080);

		// List<Transport> transports = new ArrayList<>(2);
		// transports.add(new WebSocketTransport(new StandardWebSocketClient()));
		// // transports.add(new RestTemplateXhrTransport());
		//
		// SockJsClient sockJsClient = new SockJsClient(transports);
		// ListenableFuture<WebSocketSession> handshake = sockJsClient.doHandshake(new SimpleClientWebSocketHandler(), "ws://localhost:8080/hello/websocket");
		// WebSocketSession session = handshake.get();

		StandardWebSocketClient client = new StandardWebSocketClient();
		// ListenableFuture<WebSocketSession> handshake = client.doHandshake(new MyWebSocketHandler(), "ws://localhost:8080/hello/websocket");
		// WebSocketSession session = handshake.get();
		// session.sendMessage(new TextMessage("hallo"));
		// session.close();

		// SimpleClientWebSocketHandler handler = new SimpleClientWebSocketHandler();
		//
		// WebSocketConnectionManager manager = new WebSocketConnectionManager(client, handler, "ws://localhost:8080/hello/websocket");
		// manager.start();
		System.out.println("finish");
	}

	/**
	 *
	 */
	static class MyWebSocketHandler implements WebSocketHandler {

		@Override
		public void afterConnectionEstablished(WebSocketSession session) throws Exception {
			LOGGER.info("afterConnectionEstablished");
			session.sendMessage(new TextMessage("{}"));
		}

		@Override
		public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
			LOGGER.info("handleMessage");
		}

		@Override
		public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
			LOGGER.info("handleTransportError");
		}

		@Override
		public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
			LOGGER.info("afterConnectionClosed");
		}

		@Override
		public boolean supportsPartialMessages() {
			LOGGER.info("supportsPartialMessages");
			return true;
		}

	}

	private static abstract class AbstractTestSessionHandler extends StompSessionHandlerAdapter {

		private static final Logger LOGGER = LoggerFactory.getLogger(AbstractTestSessionHandler.class);

		@Override
		public void handleFrame(StompHeaders headers, Object payload) {
			LOGGER.error("STOMP ERROR frame: " + headers.toString());
		}

		@Override
		public void handleException(StompSession s, StompCommand c, StompHeaders h, byte[] p, Throwable ex) {
			LOGGER.error("Handler exception", ex);
		}

		@Override
		public void handleTransportError(StompSession session, Throwable ex) {
			LOGGER.error("Transport failure", ex);
		}
	}

}
