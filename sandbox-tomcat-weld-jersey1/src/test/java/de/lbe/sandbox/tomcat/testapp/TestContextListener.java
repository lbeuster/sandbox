package de.lbe.sandbox.tomcat.testapp;

import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * @author lars.beuster
 */
@WebListener
public class TestContextListener implements ServletContextListener {

	@Inject
	BeanManager beanManager;

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		System.out.println("Listener.init: " + beanManager);
		// TODO Auto-generated method stub

	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub

	}
}
