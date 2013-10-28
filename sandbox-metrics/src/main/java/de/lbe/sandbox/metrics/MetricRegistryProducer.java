package de.lbe.sandbox.metrics;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.management.MBeanServer;

import com.codahale.metrics.Metric;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.jvm.BufferPoolMetricSet;
import com.codahale.metrics.jvm.FileDescriptorRatioGauge;
import com.codahale.metrics.jvm.GarbageCollectorMetricSet;
import com.codahale.metrics.jvm.MemoryUsageGaugeSet;
import com.codahale.metrics.jvm.ThreadStatesGaugeSet;

/**
 * @author lbeuster
 */
public class MetricRegistryProducer {

	/**
	 * 
	 */
	@Produces
	@ApplicationScoped
	public MetricRegistry metricRegistry(MBeanServer mbeanServer) {
		MetricRegistry registry = new MetricRegistry();

		register(new GarbageCollectorMetricSet(), registry);
		register(new BufferPoolMetricSet(mbeanServer), registry);
		register(new MemoryUsageGaugeSet(), registry);
		register(new ThreadStatesGaugeSet(), registry);
		register(new FileDescriptorRatioGauge(), registry);
		return registry;
	}

	/**
	 *
	 */
	private void register(Metric metric, MetricRegistry registry) {
		String prefix = MetricRegistry.name(metric.getClass());
		registry.register(prefix, metric);
	}
}
