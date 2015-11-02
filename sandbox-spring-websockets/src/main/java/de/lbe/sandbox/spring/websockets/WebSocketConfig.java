package de.lbe.sandbox.spring.websockets;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.SockJsServiceRegistration;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.StompWebSocketEndpointRegistration;
import org.springframework.web.socket.server.standard.TomcatRequestUpgradeStrategy;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		config.enableSimpleBroker("/topic");
		config.setApplicationDestinationPrefixes("/app");
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {

		StompWebSocketEndpointRegistration registration = registry.addEndpoint("/hello");
		// DefaultHandshakeHandler handler = new DefaultHandshakeHandler(new TomcatRequestUpgradeStrategy());
		// registration.setHandshakeHandler(handler);
		registration.withSockJS();

		// The test classpath includes both Tomcat and Jetty, so let's be explicit
		// registry.addEndpoint("/portfolio").setHandshakeHandler(handler).withSockJS();
	}
}