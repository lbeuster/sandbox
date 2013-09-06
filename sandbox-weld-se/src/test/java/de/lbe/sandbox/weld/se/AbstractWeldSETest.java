package de.lbe.sandbox.weld.se;

import javax.enterprise.inject.spi.BeanManager;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.junit.After;
import org.junit.Before;

import de.asideas.lib.commons.test.junit.AbstractJUnit4Test;

/**
 * @author Lars Beuster
 */
public abstract class AbstractWeldSETest extends AbstractJUnit4Test {

	protected Weld weld = null;

	protected BeanManager beanManager = null;

	/**
     *
     */
	@Before
	public void initWeld() {
		Weld weld = new Weld();
		WeldContainer weldContainer = weld.initialize();
		this.beanManager = weldContainer.getBeanManager();
	}

	/**
     *
     */
	@After
	public void destroyWeld() {
		if (this.weld != null) {
			this.weld.shutdown();
			this.weld = null;
		}
	}
}
