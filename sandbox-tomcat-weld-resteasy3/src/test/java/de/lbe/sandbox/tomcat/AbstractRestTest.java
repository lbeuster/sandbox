package de.lbe.sandbox.tomcat;

import java.net.URISyntaxException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.junit.Before;

import de.asideas.lib.commons.test.junit.AbstractJUnit4Test;

/**
 * @author lars.beuster
 */
public abstract class AbstractRestTest extends AbstractJUnit4Test {

	private Client restClient;

	protected TestTomcat tomcat;

	@Before
	public void setUp() {
		this.tomcat = TestTomcat.boot();
		this.restClient = ClientBuilder.newClient();
	}

	protected WebTarget prepareClient() throws URISyntaxException {
		return this.restClient.target(tomcat.getWebappContextURL().toURI());
	}
}
