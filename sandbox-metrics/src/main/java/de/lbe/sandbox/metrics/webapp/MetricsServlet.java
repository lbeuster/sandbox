package de.lbe.sandbox.metrics.webapp;

import javax.inject.Inject;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.codahale.metrics.servlets.AdminServlet;
import com.codahale.metrics.servlets.HealthCheckServlet;

/**
 * 
 */
@WebServlet(urlPatterns = "/metrics")
public class MetricsServlet extends AdminServlet {

	@Inject
	private HealthCheckRegistry healthCheckRegistry;

	@Inject
	private MetricRegistry metricRegistry;

	public void init(ServletConfig config) throws ServletException {
		config.getServletContext().setAttribute(HealthCheckServlet.HEALTH_CHECK_REGISTRY, healthCheckRegistry);
		config.getServletContext().setAttribute(com.codahale.metrics.servlets.MetricsServlet.METRICS_REGISTRY, metricRegistry);
		super.init(config);
	}
}
