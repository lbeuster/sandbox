package de.lbe.sandbox.tomee;

import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;

import com.zanox.lib.commons.arquillian.AbstractJUnit4ArquillianTest;

/**
 * @author lars.beuster
 */
public class WarArquillianTest extends AbstractJUnit4ArquillianTest {

	@Inject
	private BeanManager beanManager;

	@Inject
	private TestService testService;

	/**
	 * 
	 */
	@Deployment
	public static WebArchive deployment() {
		WebArchive archive = ShrinkWrap.create(WebArchive.class).addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
		return archive;
	}

	/**
	 * In contrast to JBoss we can use the complete test-classpath we we are inside the container. With JBoss we only have the module-classloader
	 * without the test-classpath.
	 * 
	 * TomEE doesn't deploy Jars as BeanArchives (by default?).
	 */
	@Test
	public void testTomEERecognizeWarAsBeanArchive() throws Exception {
		// System.out.println(ClassLoaderUtils.toString(Thread.currentThread().getContextClassLoader()));
		assertNotNull(this.beanManager);
		assertNotNull(this.testService);
	}

	/**
	 * 
	 */
	public static class TestService {
		// no impl
	}
}
