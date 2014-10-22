package de.lbe.sandbox.spring.security;

import org.junit.Before;

import de.asideas.lib.commons.test.junit.AbstractJUnit4Test;
import de.asideas.lib.commons.test.restclient.RestClient;
import de.asideas.lib.commons.tomcat.embedded.test.SingletonEmbeddedTestTomcat;

public abstract class AbstractTest extends AbstractJUnit4Test {

	protected RestClient client;

	@Before
	public void setUpTomcat() throws Exception {
		MyEmbeddedTomcat tomcat = SingletonEmbeddedTestTomcat.ensureIsBooted(MyEmbeddedTomcat.class);
		this.client = RestClient.create(tomcat.getWebappContextURL());

	}
}