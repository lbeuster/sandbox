package de.lbe.weld.test;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Singleton;

import org.junit.Test;

import de.asideas.lib.commons.test.junit.AbstractJUnit4Test;
import de.lbe.weld.test.EmbeddedWeld;

/**
 * @author lars.beuster
 */
public class PreDestroyTest extends AbstractJUnit4Test {

	/**
	 * @PreDestroy is never called on singleton beans.
	 */
	@Test
	public void testOnSingletonBean() {

		assertEquals(0, SingletonTestService.postConstructCalls);
		assertEquals(0, SingletonTestService.preDestroyCalls);

		// lifecycle
		EmbeddedWeld weld = new EmbeddedWeld();
		weld.start();
		weld.addBeanClass(SingletonTestService.class);
		SingletonTestService service = weld.getBean(SingletonTestService.class);
		service.service();
		weld.stop();

		// assert
		assertEquals(1, SingletonTestService.postConstructCalls);
		assertEquals(0, SingletonTestService.preDestroyCalls);
	}

	/**
	 * @PreDestroy is called on application scoped beans.
	 */
	@Test
	public void testOnApplicationScopedBean() {

		assertEquals(0, ApplicationScopedTestService.postConstructCalls);
		assertEquals(0, ApplicationScopedTestService.preDestroyCalls);

		// lifecycle
		EmbeddedWeld weld = new EmbeddedWeld();
		weld.start();
		weld.addBeanClass(ApplicationScopedTestService.class);
		ApplicationScopedTestService service = weld.getBean(ApplicationScopedTestService.class);
		service.service();
		weld.stop();

		// assert
		assertEquals(1, ApplicationScopedTestService.postConstructCalls);
		assertEquals(1, ApplicationScopedTestService.preDestroyCalls);
	}

	/**
	 * 
	 */
	@Singleton
	public static class SingletonTestService {

		static int postConstructCalls = 0;

		static int preDestroyCalls = 0;

		@PostConstruct
		void postConstruct() {
			postConstructCalls++;
		}

		@PreDestroy
		void preDestroy() {
			preDestroyCalls++;
		}

		public void service() {
			// no impl
		}
	}

	/**
	 * 
	 */
	@ApplicationScoped
	public static class ApplicationScopedTestService {

		static int postConstructCalls = 0;

		static int preDestroyCalls = 0;

		@PostConstruct
		void postConstruct() {
			postConstructCalls++;
		}

		@PreDestroy
		void preDestroy() {
			preDestroyCalls++;
		}

		public void service() {
			// no impl
		}
	}
}
