package de.lbe.sandbox.servlet30;

import org.junit.After;
import org.junit.Before;

import de.asideas.lib.commons.test.junit.AbstractJUnit4Test;
import de.lbe.lib.commons.old.httpclient.DefaultHttpClient;
import de.lbe.lib.commons.old.jetty.test.EmbeddedTestJetty;

/**
 * Provides a Jetty start up for testing against Jetty throughout several test classes.
 * 
 * @author lars.beuster
 */
public class AbstractJettyTest extends AbstractJUnit4Test {

	private EmbeddedTestJetty jetty;

	protected DefaultHttpClient httpClient;

	/**
	 * 
	 */
	@Before
	public void setUp() throws Exception {
		this.jetty = new EmbeddedTestJetty();
		this.jetty.setHttpConnectorPort(1234);
		this.jetty.start();

		this.httpClient = new DefaultHttpClient(this.jetty.getWebappContextURL());
	}

	/**
	 * 
	 */
	@After
	public void tearDown() throws Exception {
		if (this.httpClient != null) {
			this.httpClient.shutdown();
			this.httpClient = null;
		}
		if (this.jetty != null) {
			this.jetty.stop();
			this.jetty = null;
		}
	}
}
