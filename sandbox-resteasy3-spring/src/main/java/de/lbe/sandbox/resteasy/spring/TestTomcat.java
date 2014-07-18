package de.lbe.sandbox.resteasy.spring;

import java.io.IOException;

import javax.servlet.ServletException;

import org.jboss.resteasy.plugins.server.servlet.ResteasyBootstrap;

import com.ryantenney.metrics.spring.servlets.MetricsServletsContextListener;

import de.asideas.lib.commons.tomcat.embedded.EmbeddedTomcat;

/**
 * @author lars.beuster
 */
public class TestTomcat extends EmbeddedTomcat {

	@Override
	protected void initContext() throws ServletException, IOException {
		super.initContext();
		addServletListener(ResteasyBootstrap.class);

		// configure Spring
		// addServletListener(ContextLoaderListener.class);
		addServletListener(Spring4ContextLoaderListener.class);
		addServletListener(MetricsServletsContextListener.class);
		addContextParam("contextClass", MySpringApplicationContext.class.getName());

		// addContextParam("contextConfigLocation", "classpath:context-sandbox.xml");
	}
}