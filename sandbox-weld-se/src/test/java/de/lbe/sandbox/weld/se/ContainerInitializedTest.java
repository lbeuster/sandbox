package de.lbe.sandbox.weld.se;

import org.junit.Test;

import com.zanox.lib.commons.cdi.BeanManagerUtils;

/**
 * @author Lars Beuster
 */
public class ContainerInitializedTest extends AbstractWeldSETest {

	/**
     *
     */
	@Test
	public void testContainerInitializedEvent() {
		ContainerInitializedObserver observer = BeanManagerUtils.getContextualReference(this.beanManager, ContainerInitializedObserver.class);
		assertTrue(observer.isContainerInitialized());
	}
}
