package de.lbe.sandbox.spring.security;

import javax.inject.Inject;

import org.junit.Before;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.web.WebAppConfiguration;

import de.asideas.ipool.commons.lib.spring.boot.context.embedded.EmbeddedServletContainerSupport;
import de.asideas.ipool.commons.lib.spring.boot.test.AbstractSpringBootIT;
import de.asideas.lib.commons.test.restclient.RestClient;
import de.asideas.lib.commons.test.restclient.RestTarget;

/**
 * @author lbeuster
 */
@SpringApplicationConfiguration(classes = AbstractTest.TestConfiguration.class)
@WebAppConfiguration
public abstract class AbstractTest extends AbstractSpringBootIT {

	protected RestClient restClient;

	@Inject
	private SecurityProperties securityProperties;

	@Inject
	private EmbeddedServletContainerSupport tomcat;

	/**
	 *
	 */
	@Before
	public void setUpClient() {
		this.restClient = new RestClient(tomcat.getWebappContextURL());
	}

	/**
     *
	 */
	protected RestTarget<?> addBasicAuth(RestTarget<?> target) throws Exception {
		return target.basicAuth(securityProperties.getUser().getName(), securityProperties.getUser().getPassword());
	}

	/**
	 *
	 */
	@Configuration
	@Import({ EmbeddedServletContainerSupport.class, Main.class })
	static class TestConfiguration {
	}
}