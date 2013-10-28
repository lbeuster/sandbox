package de.lbe.sandbox.metrics.health;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import com.codahale.metrics.health.HealthCheck;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.codahale.metrics.health.jvm.ThreadDeadlockHealthCheck;

import de.lbe.sandbox.metrics.MetricNameUtils;
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
	@HealthChecked
	private Instance<HealthCheck> healthCheckCandidates;

	/**
	 * 
	 */
	@PostConstruct
	protected void registerAll() {
		for (HealthCheck healthCheck : this.healthCheckCandidates.select()) {
			registerIfAccepted(healthCheck);
		}

		// built-in checks
		registerIfAccepted(new ThreadDeadlockHealthCheck());
	}

	/**
	 * 
	 */
	protected void registerIfAccepted(HealthCheck healthCheck) {
		if (accept(healthCheck)) {
			String name = MetricNameUtils.forHealthCheck(healthCheck);
			this.registry.register(name, healthCheck);
		}
	}

	/**
	 * 
	 */
	protected boolean accept(HealthCheck healthCheck) {
		return true;
	}
}
