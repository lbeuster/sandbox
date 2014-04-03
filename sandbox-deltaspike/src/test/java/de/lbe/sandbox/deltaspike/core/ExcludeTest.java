package de.lbe.sandbox.deltaspike.core;

import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

import org.junit.Test;

import de.asideas.lib.commons.cdi.BeanManagerUtils;

/**
 * @author lars.beuster
 */
public class ExcludeTest extends AbstractCoreTest {

	@Inject
	private BeanManager beanManager;

	/**
	 * 
	 */
	@Test
	public void testLars() {
		// System.getProperties().list(System.out);
		ExcludedBean bean = BeanManagerUtils.getContextualReference(this.beanManager, ExcludedBean.class);
	}
}
