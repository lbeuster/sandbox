package de.lbe.weld;

import java.lang.annotation.Annotation;

import javax.inject.Inject;

import org.jboss.weld.manager.BeanManagerImpl;

import de.asideas.lib.commons.arquillian.AbstractJUnit4ArquillianTest;
import de.asideas.lib.commons.cdi.BeanManagerUtils;

/**
 * @author lars.beuster
 */
public abstract class AbstractWeldTest extends AbstractJUnit4ArquillianTest {

	/**
	 * 
	 */
	@Inject
	protected BeanManagerImpl beanManager;

	/**
	 *
	 */
	protected <T> T getBean(Class<T> beanType, Annotation... qualifiers) {
		return BeanManagerUtils.getContextualReference(this.beanManager, beanType, qualifiers);
	}
}
