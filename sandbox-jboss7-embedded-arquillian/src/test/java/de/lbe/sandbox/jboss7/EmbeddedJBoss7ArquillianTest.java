package de.lbe.sandbox.jboss7;

import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.zanox.lib.commons.lang.ClassLoaderUtils;
import com.zanox.lib.commons.shrinkwrap.ShrinkWrapUtils;

/**
 * @author lars.beuster
 */
@RunWith(JBossArquillian.class)
public class EmbeddedJBoss7ArquillianTest {

	@Inject
	private BeanManager beanManager;

	@Inject
	private TestService testService;

	/**
	 * 
	 */
	@Deployment
	public static JavaArchive deployment() {
		JavaArchive archive = ShrinkWrapUtils.prepareCdiJar("jboss-embedded.jar");

		// needed to load the TestCase inside JBoss
		// archive.addClass(EmbeddedJBoss7ArquillianTest.class);
		// archive.addClass(EmbeddedJBoss7ArquillianTest.TestService.class);
		archive.addClass(JBossArquillian.class);
		return archive;
	}

	/**
	 * 
	 */
	@Test
	public void testModuleClassLoader() throws Exception {
		Assert.assertNotNull(this.beanManager);
		Assert.assertNotNull(this.testService);

		try {
			System.out.println(ClassLoaderUtils.toString(getClass().getClassLoader()));
			Assert.fail("JBoss-module-classloader");
		} catch (NoClassDefFoundError ex) {
			// expected since we have a different runtime-CL (JBoss-module-CL)
		}
	}

	/**
	 * 
	 */
	@Singleton
	public static class TestService {
		// no impl
	}
}
