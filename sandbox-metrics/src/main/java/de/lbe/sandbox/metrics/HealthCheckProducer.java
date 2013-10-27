package de.lbe.sandbox.metrics;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.Typed;
import javax.enterprise.util.AnnotationLiteral;

import com.codahale.metrics.health.HealthCheck;
import com.codahale.metrics.health.HealthCheckRegistry;

import de.asideas.lib.commons.cdi.Startup;

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

	/**
	 * 
	 */
	@Produces
	@Startup
	@ApplicationScoped
	public HealthChecks healthChecks(@HealthChecked Instance<HealthCheck> healthCheckCandidates) {
		HealthChecks healthChecks = new HealthChecks();
		for (HealthCheck healthCheck : healthCheckCandidates.select()) {
			String name = MetricNameUtils.forHealthCheck(healthCheck);
			healthChecks.add(name, healthCheck);
		}
		return healthChecks;
	}

	/**
	 *
	 */
	@SuppressWarnings("all")
	static final class HealthCheckedLiteral extends AnnotationLiteral<HealthChecked> implements HealthChecked {

		private static final long serialVersionUID = 1L;

		static final HealthChecked INSTANCE = new HealthCheckedLiteral();

		private HealthCheckedLiteral() {
		}

		@Override
		public String value() {
			return "";
		}
	}

	/**
	 * 
	 */
	@Typed
	public static class HealthChecks {

		private final Map<String, HealthCheck> healthChecks = new HashMap<>();

		void add(String name, HealthCheck healthCheck) {
			this.healthChecks.put(name, healthCheck);
		}

		public Set<Map.Entry<String, HealthCheck>> entries() {
			return Collections.unmodifiableMap(this.healthChecks).entrySet();
		}

		@Override
		public String toString() {
			return "HealthChecks[" + this.healthChecks + "]";
		}
	}
}
