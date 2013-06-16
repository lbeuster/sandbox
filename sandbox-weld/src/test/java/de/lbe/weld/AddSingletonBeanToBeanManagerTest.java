package de.lbe.weld;

import javax.enterprise.inject.Typed;
import javax.enterprise.inject.UnsatisfiedResolutionException;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;

import com.zanox.lib.commons.shrinkwrap.ShrinkWrapUtils;

import de.lbe.weld.WeldBeanManagerUtils;

/**
 * @author lars.beuster
 */
public class AddSingletonBeanToBeanManagerTest extends AbstractWeldTest {

	/**
	 * 
	 */
	@Deployment
	public static JavaArchive createDeployment() {
		return ShrinkWrapUtils.prepareCdiJar();
	}

	/**
	 * 
	 */
	@Test
	public void testAddSingletonBean() {

		// currently our service is not in the context
		try {
			getBean(TestService.class);
			fail("not deployed");
		} catch (UnsatisfiedResolutionException ex) {
			// expected
		}

		// add the bean
		final String NAME = "TEST";
		TestService service = new TestService(NAME);
		WeldBeanManagerUtils.addSingletonBean(this.beanManager, service);

		// try again
		TestService bean = getBean(TestService.class);

		// since we get a proxy class we cannot access the fields
		assertNull(bean.name);

		// but the methods are ok
		assertEquals(NAME, bean.getName());
	}

	/**
	 * static, @Typed and the noarg-constructor are all needed to create a proxy-class.
	 */
	@Typed
	public static class TestService {

		final String name;

		public TestService(String name) {
			this.name = name;
		}

		TestService() {
			// this constructor is needed for the Weld-proxy
			this(null);
		}

		public String getName() {
			return this.name;
		}
	}
}
