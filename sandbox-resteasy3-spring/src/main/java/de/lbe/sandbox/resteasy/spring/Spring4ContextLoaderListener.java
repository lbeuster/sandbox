package de.lbe.sandbox.resteasy.spring;

import javax.servlet.ServletContext;

import org.jboss.resteasy.plugins.spring.SpringContextLoaderSupport;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.WebApplicationContext;

/**
 * @author lbeuster
 */
public class Spring4ContextLoaderListener extends org.jboss.resteasy.plugins.spring.SpringContextLoaderListener {

	private final WebApplicationContext applicationContext;

	private final SpringContextLoaderSupport springContextLoaderSupport = new SpringContextLoaderSupport();

	public Spring4ContextLoaderListener(WebApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	public Spring4ContextLoaderListener() {
		this(null);
	}

	@Override
	protected WebApplicationContext createWebApplicationContext(ServletContext sc) {
		return this.applicationContext != null ? this.applicationContext : super.createWebApplicationContext(sc);
	}

	@Override
	protected void customizeContext(ServletContext servletContext, ConfigurableWebApplicationContext configurableWebApplicationContext) {
		super.customizeContext(servletContext, configurableWebApplicationContext);

		// Spring 4 support
		this.springContextLoaderSupport.customizeContext(servletContext, configurableWebApplicationContext);
	}
}