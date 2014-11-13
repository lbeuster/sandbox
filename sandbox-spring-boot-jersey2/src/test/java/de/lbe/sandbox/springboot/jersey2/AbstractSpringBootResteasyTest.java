package de.lbe.sandbox.springboot.jersey2;

import java.net.URI;

import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;

import org.glassfish.jersey.client.JerseyClientBuilder;
import org.junit.After;
import org.junit.Before;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.web.WebAppConfiguration;

import de.asideas.lib.commons.spring.boot.test.AbstractSpringBootIT;
import de.asideas.lib.commons.spring.boot.test.EmbeddedTestServletContainer;

/**
 * @author lbeuster
 */
@SpringApplicationConfiguration(classes = AbstractSpringBootResteasyTest.TestConfiguration.class)
@WebAppConfiguration
public abstract class AbstractSpringBootResteasyTest extends AbstractSpringBootIT {

	private Client restClient;

	@Inject
	private EmbeddedTestServletContainer container;

	private URI webappContextURL;

	@Before
	public void setUp() throws Exception {
		JerseyClientBuilder builder = new JerseyClientBuilder();
		this.restClient = builder.build();
		this.webappContextURL = this.container.getWebappContextURL();
	}

	@After
	public void tearDown() {
		if (this.restClient != null) {
			this.restClient.close();
		}
	}

	protected WebTarget target(String path) {
		return this.restClient.target(this.webappContextURL).path(path);
	}

	/**
	 *
	 */
	@Configuration
	@Import({ HelloConfiguration.class, EmbeddedTestServletContainer.class })
	public static class TestConfiguration {
		// no impl - just the annotations
	}
}