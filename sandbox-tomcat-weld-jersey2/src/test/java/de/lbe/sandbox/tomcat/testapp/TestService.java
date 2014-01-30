package de.lbe.sandbox.tomcat.testapp;

import javax.validation.Valid;

/**
 * @author lars.beuster
 */
public class TestService {

	// @Inject
	// Event<TestEvent> eventPublisher;
	//
	// public void service() {
	// eventPublisher.fire(new TestEvent());
	// }

	public TestBean service(@Valid TestBean testBean) {
		return testBean;

	}
}
