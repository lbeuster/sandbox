package de.lbe.sandbox.openejb;

import org.junit.runner.Description;

import de.asideas.lib.commons.test.junit.rules.AbstractTestWatcher;

/**
 * @author lars.beuster
 */
public class OpenEJBRule extends AbstractTestWatcher {

	protected void setUp(Description description) throws Throwable {
		OpenEJBContainer.start();
	}

	void bind(Object testCase) throws Exception {
		OpenEJBContainer.getContext().bind("inject", testCase);
	}
}
