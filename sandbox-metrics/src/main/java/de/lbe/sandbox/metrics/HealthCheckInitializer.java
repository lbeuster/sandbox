package de.lbe.sandbox.metrics;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.codahale.metrics.health.HealthCheck;
import com.codahale.metrics.health.HealthCheckRegistry;

import de.lbe.sandbox.metrics.HealthCheckProducer.HealthChecks;
import de.asideas.lib.commons.cdi.Startup;

/**
 * @author lbeuster
 */
@Startup
@ApplicationScoped
public class HealthCheckInitializer {

	@Inject
	protected HealthCheckRegistry registry;

	@Inject
	protected HealthChecks healthChecks;

	/**
	 * 
	 */
	@PostConstruct
	protected void registerAll() {
		for (Map.Entry<String, HealthCheck> entry : this.healthChecks.entries()) {
			HealthCheck healthCheck = entry.getValue();
			if (accept(healthCheck)) {
				this.registry.register(entry.getKey(), healthCheck);
			}
		}
	}

	/**
	 * 
	 */
	protected boolean accept(HealthCheck healthCheck) {
		return true;
	}
}
