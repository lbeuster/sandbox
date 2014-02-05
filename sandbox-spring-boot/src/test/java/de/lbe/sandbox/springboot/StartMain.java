package de.lbe.sandbox.springboot;

import org.junit.Test;

import de.asideas.lib.commons.test.junit.AbstractJUnit4Test;

/**
 * @author lars.beuster
 */
public class StartMain extends AbstractJUnit4Test {

	@Test
	public void start() {
		new Main().start(false);
		System.out.println("finished");
	}
}
