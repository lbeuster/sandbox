package de.lbe.sandbox.spring.websockets;

import javax.inject.Inject;

import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;

import de.asideas.ipool.commons.lib.spring.boot.context.embedded.EmbeddedServletContainerSupport;
import de.asideas.ipool.commons.lib.spring.boot.test.AbstractSpringBootIT;

/**
 * @author lbeuster
 */
@SpringApplicationConfiguration(classes = AbstractWebsocketTest.TestConfiguration.class)
@WebAppConfiguration
@ActiveProfiles("test")
public abstract class AbstractWebsocketTest extends AbstractSpringBootIT {

	@Inject
	private EmbeddedServletContainerSupport container;

	/**
	 *
	 */
	@Configuration
	@Import({ Main.class, EmbeddedServletContainerSupport.class })
	public static class TestConfiguration {
		// no impl - just the annotations
	}
}