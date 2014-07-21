package de.lbe.sandbox.resteasy.spring;

import javax.servlet.ServletContext;

import org.jboss.resteasy.plugins.server.servlet.ResteasyBootstrap;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import com.ryantenney.metrics.spring.servlets.MetricsServletsContextListener;

public class MyWebAppInitializer implements WebApplicationInitializer {

	@Override
	public void onStartup(ServletContext container) {

		// Create the 'root' Spring application context
		AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
		rootContext.register(MethodValidationPostProcessor.class);
		rootContext.scan("de.lbe");
		//
		// Manage the lifecycle of the root application context
		container.addListener(ResteasyBootstrap.class);
		container.addListener(new Spring4ContextLoaderListener(rootContext));
		container.addListener(MetricsServletsContextListener.class);

		//
		// // Create the dispatcher servlet's Spring application context
		// AnnotationConfigWebApplicationContext dispatcherContext = new AnnotationConfigWebApplicationContext();
		// dispatcherContext.register(DispatcherConfig.class);
		//
		// // Register and map the dispatcher servlet
		// ServletRegistration.Dynamic dispatcher = container.addServlet("dispatcher", new DispatcherServlet(dispatcherContext));
		// dispatcher.setLoadOnStartup(1);
		// dispatcher.addMapping("/");
	}

}