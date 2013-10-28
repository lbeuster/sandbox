package de.lbe.sandbox.metrics.health;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import com.codahale.metrics.health.HealthCheckRegistry;

/**
 * @author lbeuster
 */
public class HealthCheckProducer {

	/**
	 * 
	 */
	@Produces
	@ApplicationScoped
	public HealthCheckRegistry healthCheckRegistry() {
		return new HealthCheckRegistry();
	}
}
