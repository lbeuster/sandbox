package de.lbe.sandbox.springboot;

import javax.inject.Inject;

import org.junit.After;
import org.junit.Before;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;

import de.asideas.ipool.commons.lib.spring.boot.context.embedded.EmbeddedServletContainerSupport;
import de.asideas.ipool.commons.lib.spring.boot.test.AbstractSpringBootIT;
import de.asideas.lib.commons.test.restclient.RestClient;

/**
 * @author lbeuster
 */
@SpringApplicationConfiguration(classes = AbstractSpringBootTest.TestConfiguration.class)
@WebAppConfiguration
@ActiveProfiles("test")
public abstract class AbstractSpringBootTest extends AbstractSpringBootIT {

	protected RestClient restClient;

	@Inject
	private EmbeddedServletContainerSupport container;

	@Before
	public void setUp() throws Exception {
		this.restClient = new RestClient(container.getWebappContextURL());

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
	@Import({ Main.class, EmbeddedServletContainerSupport.class })
	public static class TestConfiguration {
		// no impl - just the annotations
	}
}