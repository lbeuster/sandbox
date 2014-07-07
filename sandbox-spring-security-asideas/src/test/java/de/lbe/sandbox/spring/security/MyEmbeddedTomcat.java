package de.lbe.sandbox.spring.security;

import java.io.IOException;

import javax.servlet.ServletException;

import org.jboss.resteasy.plugins.server.servlet.ResteasyBootstrap;
import org.springframework.web.filter.DelegatingFilterProxy;

import de.asideas.lib.commons.tomcat.embedded.EmbeddedTomcat;

public class MyEmbeddedTomcat extends EmbeddedTomcat {

	public MyEmbeddedTomcat() {
		// setWebappDir("src/main/webapp");
	}

	@Override
	protected void initContext() throws ServletException, IOException {
		super.initContext();
		addServletListener(ResteasyBootstrap.class);
		addServletListener(Spring4ContextLoaderListener.class);
		addContextParam("contextConfigLocation", "classpath:context.xml");
		addFilter(DelegatingFilterProxy.class, "springSecurityFilterChain", "/*");
	}
}