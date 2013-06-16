package de.lbe.weld.test;

import javax.enterprise.inject.UnsatisfiedResolutionException;
import javax.inject.Inject;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.zanox.lib.commons.test.junit.AbstractJUnit4Test;

import de.lbe.weld.test.EmbeddedWeld;

/**
 * @author lars.beuster
 */
public class EmbeddedWeldTest extends AbstractJUnit4Test {

	private EmbeddedWeld weld;

	/**
	 * 
	 */
	@Before
	public void setUp() {
		this.weld = new EmbeddedWeld();
		this.weld.start();
	}

	/**
	 * 
	 */
	@After
	public void tearDown() {
		this.weld.stop();
		this.weld = null;
	}

	/**
	 * 
	 */
	@Test
	public void testAddSingletonBean() {

		// we have no service
		try {
			this.weld.getBean(TestService.class);
			fail("bean not available");
		} catch (UnsatisfiedResolutionException ex) {
			// expected
		}

		// we add a singleton bean
		final String NAME = "TEST";
		TestService service = new TestService();
		service.setName(NAME);
		this.weld.addSingletonBean(service);

		service = this.weld.getBean(TestService.class);
		assertEquals(NAME, service.getName());
	}

	/**
	 * 
	 */
	@Test
	public void testAddBeanClass() {

		// we have no service
		try {
			this.weld.getBean(TestService.class);
			fail("bean not available");
		} catch (UnsatisfiedResolutionException ex) {
			// expected
		}

		// we add the bean class
		this.weld.addBeanClass(TestService.class);
		TestService service = this.weld.getBean(TestService.class);

		// assert
		assertNull(service.getName());
	}

	/**
	 * 
	 */
	@Test
	public void testInjection() {

		final String NAME = "TEST";

		// prepare
		this.weld.addBeanClass(TestService2.class);
		TestService service = new TestService();
		service.setName(NAME);
		this.weld.addSingletonBean(service);
		TestService2 service2 = this.weld.getBean(TestService2.class);

		// assert
		assertNotNull(service2.service);
		assertEquals(NAME, service2.service.getName());
	}

	/**
	 * Has to be static to to because of Weld-proxy-class.
	 */
	public static class TestService {

		private String name;

		public String getName() {
			return this.name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}

	/**
	 * 
	 */
	public static class TestService2 {

		@Inject
		TestService service;
	}
}
