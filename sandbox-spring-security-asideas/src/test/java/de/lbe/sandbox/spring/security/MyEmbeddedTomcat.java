package de.lbe.sandbox.spring.security;

import java.io.IOException;

import javax.servlet.ServletException;

import org.jboss.resteasy.plugins.server.servlet.ResteasyBootstrap;
import org.jboss.resteasy.plugins.spring.SpringContextLoaderListener;
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
		addServletListener(SpringContextLoaderListener.class);
		addContextParam(SpringContextLoaderListener.CONTEXT_CLASS_PARAM, MyApplicationContext.class.getName());
		// addContextParam("contextConfigLocation", "classpath:context.xml");
		addFilter(DelegatingFilterProxy.class, "springSecurityFilterChain", "/*");
	}
}