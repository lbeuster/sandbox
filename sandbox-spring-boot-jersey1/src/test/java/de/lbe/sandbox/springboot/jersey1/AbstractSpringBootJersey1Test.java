package de.lbe.sandbox.springboot.jersey1;

import java.net.URI;

import javax.inject.Inject;

import org.junit.After;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.web.WebAppConfiguration;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.LoggingFilter;

import de.asideas.ipool.commons.lib.spring.boot.test.AbstractSpringBootIT;
import de.asideas.ipool.commons.lib.spring.boot.test.EmbeddedTestServletContainer;

/**
 * @author lbeuster
 */
@SpringApplicationConfiguration(classes = AbstractSpringBootJersey1Test.TestConfiguration.class)
@WebAppConfiguration
public abstract class AbstractSpringBootJersey1Test extends AbstractSpringBootIT {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractSpringBootJersey1Test.class);

	private Client restClient;

	@Inject
	private EmbeddedTestServletContainer container;

	private URI webappContextURL;

	@Before
	public void setUp() throws Exception {
		DefaultClientConfig cc = new DefaultClientConfig();
		this.restClient = Client.create(cc);
		LoggingFilter loggingFilter = new LoggingFilter(java.util.logging.Logger.getLogger(LOGGER.getName()));
		restClient.addFilter(loggingFilter);

		// misc
		restClient.setConnectTimeout(1000_000);
		restClient.setReadTimeout(1000_000);

		this.webappContextURL = this.container.getWebappContextURL();
	}

	@After
	public void tearDown() {
		if (this.restClient != null) {
			this.restClient.destroy();
		}
	}

	protected WebResource target(String path) {
		return this.restClient.resource(this.webappContextURL).path(path);
	}

	/**
	 *
	 */
	@Configuration
	@EnableAutoConfiguration
	@ComponentScan
	@Import({ EmbeddedTestServletContainer.class })
	public static class TestConfiguration {
		// no impl - just the annotations
	}
}