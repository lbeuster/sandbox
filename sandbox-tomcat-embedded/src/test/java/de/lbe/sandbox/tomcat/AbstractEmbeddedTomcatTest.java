package de.lbe.sandbox.tomcat;

import org.junit.Before;

import de.asideas.lib.commons.test.junit.AbstractJUnit4Test;

/**
 * @author lars.beuster
 */
public abstract class AbstractEmbeddedTomcatTest extends AbstractJUnit4Test {

	protected TestTomcat tomcat;

	@Before
	public void setUp() {
		this.tomcat = TestTomcat.boot();
	}
}
