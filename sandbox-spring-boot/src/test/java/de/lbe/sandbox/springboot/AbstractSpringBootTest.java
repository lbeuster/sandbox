package de.lbe.sandbox.springboot;

import javax.inject.Inject;

import org.junit.After;
import org.junit.Before;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.web.WebAppConfiguration;

import de.asideas.lib.commons.spring.boot.test.AbstractSpringBootIT;
import de.asideas.lib.commons.spring.boot.test.EmbeddedTestServletContainer;
import de.asideas.lib.commons.test.restclient.RestClient;

/**
 * @author lbeuster
 */
@SpringApplicationConfiguration(classes = AbstractSpringBootTest.TestConfiguration.class)
@WebAppConfiguration
public abstract class AbstractSpringBootTest extends AbstractSpringBootIT {

	protected RestClient restClient;

	@Inject
	private EmbeddedTestServletContainer container;

	@Before
	public void setUp() throws Exception {
		this.restClient = RestClient.create(container.getWebappContextURL());

	}

	@After
	public void tearDown() {
		if (this.restClient != null) {
			this.restClient.close();
		}
	}

	/**
	 *
	 */
	@Configuration
	@Import({ Main.class, EmbeddedTestServletContainer.class })
	public static class TestConfiguration {
		// no impl - just the annotations
	}
}