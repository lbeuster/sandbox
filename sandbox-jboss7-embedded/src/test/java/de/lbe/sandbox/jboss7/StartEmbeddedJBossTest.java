package de.lbe.sandbox.jboss7;

import org.jboss.as.embedded.EmbeddedServerFactory;
import org.jboss.as.embedded.StandaloneServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.zanox.lib.commons.test.junit.AbstractJUnit4Test;

/**
 * @author Lars Beuster
 */
public class StartEmbeddedJBossTest extends AbstractJUnit4Test {

	private StandaloneServer server;

	@Before
	public void setUp() throws Exception {
		server = EmbeddedServerFactory.create(JBoss.JBOSS_HOME, null, null);
		server.start();
	}

	@After
	public void tearDown() {
		if (server != null) {
			server.stop();
		}
	}

	@Test
	public void testJboss() throws Exception {
		assertNotNull(server);
		System.out.println("started");
	}
}
