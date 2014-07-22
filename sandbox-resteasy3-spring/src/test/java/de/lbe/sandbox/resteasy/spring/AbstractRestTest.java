package de.lbe.sandbox.resteasy.spring;

import org.junit.Before;

import de.asideas.lib.commons.test.junit.AbstractJUnit4Test;
import de.asideas.lib.commons.test.restclient.RestClient;
import de.asideas.lib.commons.tomcat.embedded.test.SingletonEmbeddedTestTomcat;
import de.lbe.sandbox.resteasy.spring.boot.TestTomcat;

/**
 * @author lars.beuster
 */
public abstract class AbstractRestTest extends AbstractJUnit4Test {

	protected TestTomcat tomcat;

	protected RestClient client;

	@Before
	public void setUp() {
		this.tomcat = SingletonEmbeddedTestTomcat.ensureIsBooted(TestTomcat.class);
		this.client = RestClient.create(this.tomcat.getWebappContextURL());
	}
}
