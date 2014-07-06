package de.lbe.sandbox.resteasy.spring;

import javax.annotation.Priority;
import javax.servlet.ServletContext;
import javax.servlet.annotation.WebListener;

import org.jboss.resteasy.plugins.spring.SpringContextLoaderSupport;
import org.springframework.web.context.ConfigurableWebApplicationContext;

public class Spring4ContextLoaderListener extends org.jboss.resteasy.plugins.spring.SpringContextLoaderListener {

	private SpringContextLoaderSupport springContextLoaderSupport = new SpringContextLoaderSupport();

	{
		System.out.println(getClass().getName() + ".<init>");
	}

	// @Override
	// public void contextInitialized(ServletContextEvent event) {
	// boolean scanProviders = false;
	// boolean scanResources = false;
	//
	// String sProviders = event.getServletContext().getInitParameter("resteasy.scan.providers");
	// if (sProviders != null) {
	// scanProviders = Boolean.valueOf(sProviders.trim());
	// }
	// String scanAll = event.getServletContext().getInitParameter("resteasy.scan");
	// if (scanAll != null) {
	// boolean tmp = Boolean.valueOf(scanAll.trim());
	// scanProviders = tmp || scanProviders;
	// scanResources = tmp || scanResources;
	// }
	// String sResources = event.getServletContext().getInitParameter("resteasy.scan.resources");
	// if (sResources != null) {
	// scanResources = Boolean.valueOf(sResources.trim());
	// }
	//
	// if (scanProviders || scanResources) {
	// throw new RuntimeException(
	// "You cannot use resteasy.scan, resteasy.scan.resources, or resteasy.scan.providers with the SpringContextLoaderLister as this may cause serious deployment errors in your application");
	// }
	//
	// super.contextInitialized(event);
	// }

	@Override
	protected void customizeContext(ServletContext servletContext, ConfigurableWebApplicationContext configurableWebApplicationContext) {
		super.customizeContext(servletContext, configurableWebApplicationContext);

		// Spring 4 support
		this.springContextLoaderSupport.customizeContext(servletContext, configurableWebApplicationContext);
	}
}