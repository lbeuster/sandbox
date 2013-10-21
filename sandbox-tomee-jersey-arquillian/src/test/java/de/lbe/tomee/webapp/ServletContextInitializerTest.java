package de.lbe.tomee.webapp;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;

import de.asideas.lib.commons.arquillian.AbstractJUnit4ArquillianTest;
import de.asideas.lib.commons.shrinkwrap.ShrinkWrapUtils;

/**
 * @author lars.beuster
 */
public class ServletContextInitializerTest extends AbstractJUnit4ArquillianTest {

	/**
	 * 
	 */
	@Deployment
	public static WebArchive deployment() {

		TestServletListener.staticEvent = null;

		// first we create a jar with the annotated servlet listener in it
		JavaArchive jar = ShrinkWrap.create(JavaArchive.class);
		jar.addClass(TestServletListener.class);

		// create a WAR
		WebArchive war = ShrinkWrapUtils.prepareCdiWar();
		war.addAsLibraries(jar);
		return war;
	}

	/**
	 * 
	 */
	@Test
	public void testServletListenerIsStarted() throws Exception {
		assertNotNull(TestServletListener.staticEvent);
	}

	/**
	 * 
	 */
	@WebListener
	public static class TestServletListener implements ServletContextListener {

		static ServletContextEvent staticEvent = null;

		@Override
		public void contextInitialized(ServletContextEvent sce) {
			staticEvent = sce;
		}

		@Override
		public void contextDestroyed(ServletContextEvent sce) {
			// no impl
		}
	}
}
