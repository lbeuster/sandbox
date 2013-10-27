package de.lbe.sandbox.metrics.webapp;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.health.HealthCheckRegistry;

/**
 * @author lbeuster
 */
public class MetricRegistryProducer {

	@Produces
	@ApplicationScoped
	public MetricRegistry metricRegistry() {
		return new MetricRegistry();
	}

	@Produces
	@ApplicationScoped
	public HealthCheckRegistry healthCheckRegistry() {
		return new HealthCheckRegistry();
	}

}
