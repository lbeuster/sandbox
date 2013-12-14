package de.lbe.sandbox.tomcat.testapp;

import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;

import org.glassfish.jersey.servlet.ServletContainer;

/**
 * @author lars.beuster
 */
@WebServlet(urlPatterns="/*", loadOnStartup = 1, initParams = { @WebInitParam(name = "javax.ws.rs.Application", value = "de.lbe.sandbox.tomcat.testapp.MyRestApplication") })
public class JerseyServlet extends ServletContainer {

	@Inject
	BeanManager beanManager;

	@Override
	public void init() throws ServletException {
		System.out.println("JerseyServlet.init: " + beanManager);
		super.init();

	}
}
