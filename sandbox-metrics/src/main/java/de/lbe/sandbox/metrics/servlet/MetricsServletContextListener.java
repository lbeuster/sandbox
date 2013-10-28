package de.lbe.sandbox.metrics.servlet;

import javax.inject.Inject;
import javax.servlet.annotation.WebListener;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.servlets.MetricsServlet;

/**
 * @author lbeuster
 */
@WebListener
public class MetricsServletContextListener extends MetricsServlet.ContextListener {

	@Inject
	private MetricRegistry metricRegistry;

	@Override
	protected MetricRegistry getMetricRegistry() {
		return this.metricRegistry;
	}
}
