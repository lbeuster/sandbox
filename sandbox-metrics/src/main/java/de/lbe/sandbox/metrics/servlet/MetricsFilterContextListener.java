package de.lbe.sandbox.metrics.servlet;

import javax.inject.Inject;
import javax.servlet.annotation.WebListener;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.servlet.InstrumentedFilterContextListener;

@WebListener
public class MetricsFilterContextListener extends InstrumentedFilterContextListener {

	@Inject
	private MetricRegistry metricRegistry;

	@Override
	protected MetricRegistry getMetricRegistry() {
		return metricRegistry;
	}
}