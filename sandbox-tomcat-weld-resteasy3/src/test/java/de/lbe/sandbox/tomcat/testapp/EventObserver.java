package de.lbe.sandbox.tomcat.testapp;

import javax.enterprise.event.Observes;
import javax.enterprise.event.TransactionPhase;

/**
 * @author lars.beuster
 */
public class EventObserver {

	public void consumeEvent(@Observes(during=TransactionPhase.AFTER_COMPLETION) TestEvent event) {
		System.out.println("consumed event: " + event);
	}
}
