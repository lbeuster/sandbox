package de.lbe.sandbox.tomcat.testapp;

import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

/**
 * @author lars.beuster
 */
@WebServlet(urlPatterns = "/test/*", loadOnStartup = 1)
public class TestServlet extends HttpServlet {

	@Inject
	BeanManager beanManager;

	@Override
	public void init() {
		System.out.println("Servlet.init: " + beanManager);

	}
}
