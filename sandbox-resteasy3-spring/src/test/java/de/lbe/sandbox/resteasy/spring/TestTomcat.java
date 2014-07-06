package de.lbe.sandbox.resteasy.spring;

import de.asideas.lib.commons.tomcat.embedded.EmbeddedTomcat;

/**
 * @author lars.beuster
 */
public class TestTomcat extends EmbeddedTomcat {

	private TestTomcat() {
		addServletListener(ResteasyBootstrapListener.class);
		addServletListener(Spring4ContextLoaderListener.class);
		addContextParam("contextConfigLocation", "classpath:context-sandbox.xml");
	}
}