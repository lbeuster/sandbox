package de.lbe.sandbox.spring.websockets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class GreetingController {

	private static final Logger LOGGER = LoggerFactory.getLogger(GreetingController.class);

	// @RequestMapping("/hello")
	@MessageMapping("/hello")
	@SendTo("/topic/greetings")
	public Greeting greeting(HelloMessage message) throws Exception {
		LOGGER.info("greeting: {}", message);
		// Thread.sleep(3000); // simulated delay
		return new Greeting("Hello, " + message.getName() + "!");
	}

}
