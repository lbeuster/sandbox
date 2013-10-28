package de.lbe.sandbox.metrics.servlet;

import javax.inject.Inject;
import javax.servlet.annotation.WebListener;

import com.codahale.metrics.health.HealthCheckRegistry;
import com.codahale.metrics.servlets.HealthCheckServlet;

/**
 * @author lbeuster
 */
@WebListener
public class HealthCheckServletContextListener extends HealthCheckServlet.ContextListener {

	@Inject
	private HealthCheckRegistry healthCheckRegistry;

	@Override
	protected HealthCheckRegistry getHealthCheckRegistry() {
		return this.healthCheckRegistry;
	}
}
