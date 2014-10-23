package de.lbe.sandbox.springboot;

import java.net.URI;

import org.junit.After;
import org.junit.Before;
import org.springframework.boot.context.embedded.AbstractConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.context.ConfigurableApplicationContext;

import de.asideas.lib.commons.spring.boot.tomcat.EmbeddedServletContainerFactories;
import de.asideas.lib.commons.test.junit.AbstractJUnit4Test;
import de.asideas.lib.commons.test.restclient.RestClient;

/**
 * @author lbeuster
 */
public abstract class AbstractSpringBootTest extends AbstractJUnit4Test {

	private ConfigurableApplicationContext applicationContext;

	protected RestClient restClient;

	@Before
	public void setUp() throws Exception {
		this.applicationContext = Main.main();
		EmbeddedServletContainerFactory factory = this.applicationContext.getBean(EmbeddedServletContainerFactory.class);
		URI webappContextURL = EmbeddedServletContainerFactories.getWebappContextURL((AbstractConfigurableEmbeddedServletContainer) factory);
		this.restClient = RestClient.create(webappContextURL);

	}

	@After
	public void tearDown() {
		if (this.restClient != null) {
			this.restClient.close();
		}
		if (this.applicationContext != null) {
			this.applicationContext.close();
		}
	}
}