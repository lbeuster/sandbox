package de.lbe.sandbox.resteasy.spring.metrics;

import javax.servlet.annotation.WebServlet;

import com.codahale.metrics.servlets.AdminServlet;

/**
 * @author lbeuster
 */
@WebServlet(name = "MetricsServlet", urlPatterns = "/admin/metrics/*", loadOnStartup = 10)
public class MetricsServlet extends AdminServlet {

	private static final long serialVersionUID = 1L;
}
