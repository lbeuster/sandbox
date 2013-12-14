package de.lbe.sandbox.tomcat.testapp;

import javax.enterprise.event.Event;
import javax.inject.Inject;

/**
 * @author lars.beuster
 */
public class TestService {

	@Inject
	Event<TestEvent> eventPublisher;

	public void service() {
		eventPublisher.fire(new TestEvent());
	}
}
