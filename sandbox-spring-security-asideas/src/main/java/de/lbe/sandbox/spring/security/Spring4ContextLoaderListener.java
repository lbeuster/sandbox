package de.lbe.sandbox.spring.security;

import javax.servlet.ServletContext;

import org.jboss.resteasy.plugins.spring.SpringContextLoaderSupport;
import org.springframework.web.context.ConfigurableWebApplicationContext;

public class Spring4ContextLoaderListener extends org.jboss.resteasy.plugins.spring.SpringContextLoaderListener {

	private SpringContextLoaderSupport springContextLoaderSupport = new SpringContextLoaderSupport();

	@Override
	protected void customizeContext(ServletContext servletContext, ConfigurableWebApplicationContext configurableWebApplicationContext) {
		super.customizeContext(servletContext, configurableWebApplicationContext);

		// Spring 4 support
		this.springContextLoaderSupport.customizeContext(servletContext, configurableWebApplicationContext);
	}
}