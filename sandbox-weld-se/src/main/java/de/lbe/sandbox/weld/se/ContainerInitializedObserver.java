package de.lbe.sandbox.weld.se;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

import org.jboss.weld.environment.se.events.ContainerInitialized;

/**
 * @author Lars Beuster
 */
@ApplicationScoped
public class ContainerInitializedObserver {

	private boolean containerInitialized = false;

	public boolean isContainerInitialized() {
		return this.containerInitialized;
	}

	void containerInitialized(@Observes ContainerInitialized event) {
		this.containerInitialized = true;
	}
}
