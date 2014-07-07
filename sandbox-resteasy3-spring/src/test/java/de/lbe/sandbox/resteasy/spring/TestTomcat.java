package de.lbe.sandbox.resteasy.spring;

import java.io.IOException;

import javax.servlet.ServletException;

import de.asideas.lib.commons.tomcat.embedded.EmbeddedTomcat;

/**
 * @author lars.beuster
 */
public class TestTomcat extends EmbeddedTomcat {

	@Override
	protected void initContext() throws ServletException, IOException {
		super.initContext();
		addServletListener(ResteasyBootstrapListener.class);
		addServletListener(Spring4ContextLoaderListener.class);
		addContextParam("contextConfigLocation", "classpath:context-sandbox.xml");
	}
}