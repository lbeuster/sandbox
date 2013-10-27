package de.lbe.sandbox.metrics;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import com.codahale.metrics.MetricRegistry;

/**
 * @author lbeuster
 */
public class MetricRegistryProducer {

	@Produces
	@ApplicationScoped
	public MetricRegistry metricRegistry() {
		return new MetricRegistry();
	}
}
