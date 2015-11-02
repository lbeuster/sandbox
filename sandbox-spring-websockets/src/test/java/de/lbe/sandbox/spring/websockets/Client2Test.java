package de.lbe.sandbox.spring.websockets;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Test;

public class Client2Test extends AbstractWebsocketTest {

	@Test
	public void testClient2() {
		try {
			// open websocket
			final WebsocketClientEndpoint clientEndPoint = new WebsocketClientEndpoint(new URI("ws://localhost:8080/hello/websocket"));

			// add listener
			clientEndPoint.addMessageHandler(new WebsocketClientEndpoint.MessageHandler() {
				@Override
				public void handleMessage(String message) {
					System.out.println(message);
				}
			});

			// send message to websocket
			clientEndPoint.sendMessage("{'event':'addChannel','channel':'ok_btccny_ticker'}");

			// wait 5 seconds for messages from websocket
			Thread.sleep(5000);

		} catch (InterruptedException ex) {
			System.err.println("InterruptedException exception: " + ex.getMessage());
		} catch (URISyntaxException ex) {
			System.err.println("URISyntaxException exception: " + ex.getMessage());
		}
	}
}