package de.lbe.sandbox.servlet30;

import org.junit.After;
import org.junit.Before;

import de.asideas.ipool.commons.lib.test.junit.AbstractJUnitTest;
import de.asideas.lib.commons.tomcat.embedded.EmbeddedTomcat;
import de.lbe.lib.commons.old.httpclient.DefaultHttpClient;

/**
 * Provides a Jetty start up for testing against Jetty throughout several test classes.
 *
 * @author lars.beuster
 */
public class AbstractTomcatTest extends AbstractJUnitTest {

	private EmbeddedTomcat tomcat;

	protected DefaultHttpClient httpClient;

	/**
	 *
	 */
	@Before
	public void setUp() throws Exception {
		this.tomcat = new EmbeddedTomcat();
		this.tomcat.setPort(1234);
		this.tomcat.start();

		this.httpClient = new DefaultHttpClient(this.tomcat.getWebappContextURL());
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
		if (this.tomcat != null) {
			this.tomcat.stop();
			this.tomcat = null;
		}
	}
}
