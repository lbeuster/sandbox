package de.lbe.sandbox.metrics;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;

public class MetricProducer {

	/**
	 * 
	 */
	@Produces
	public Timer timer(InjectionPoint ip, MetricRegistry registry) {
		String metricName = MetricNameUtils.forInjectionPoint(ip);
		return registry.timer(metricName);
	}

	/**
	 * 
	 */
	@Produces
	public Meter meter(InjectionPoint ip, MetricRegistry registry) {
		String metricName = MetricNameUtils.forInjectionPoint(ip);
		return registry.meter(metricName);
	}

	/**
	 * 
	 */
	@Produces
	public Counter counter(InjectionPoint ip, MetricRegistry registry) {
		String metricName = MetricNameUtils.forInjectionPoint(ip);
		return registry.counter(metricName);
	}
}
