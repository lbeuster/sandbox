package de.lbe.weld;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.junit.Test;

import de.asideas.lib.commons.test.junit.AbstractJUnit4Test;
import de.lbe.weld.test.EmbeddedWeld;

/**
 * @author lars.beuster
 */
public class NoArgConstructorBeanNotProxyableTest extends AbstractJUnit4Test {

	/**
	 * 
	 */
	@Test
	public void testInjectNoArgConstructorBeanIsNotProxyable() {

		// prepare
		EmbeddedWeld weld = new EmbeddedWeld();
		weld.start();
		weld.addBeanClass(TestBean.class);
		weld.addBeanClass(TestBeanWithNoArgConstructor.class);

		// we cannot get a reference to that bean
		try {
			weld.getBean(TestBeanWithNoArgConstructor.class);
			fail("not proxyable");
		} catch (Exception ex) {
			Throwable cause = ExceptionUtils.getRootCause(ex);
			assertSame(InstantiationException.class, cause.getClass());
		}
	}

	/**
	 * 
	 */
	public static class TestBean {
		// no impl
	}

	/**
	 * 
	 */
	@ApplicationScoped
	public static class TestBeanWithNoArgConstructor {

		@Inject
		public TestBeanWithNoArgConstructor(TestBean testBean) {
			assertNotNull(testBean);
		}
	}
}
