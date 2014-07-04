package de.lbe.sandbox.springboot.testapp;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.asideas.lib.commons.cdi.jmx.Domain;

/**
 * @author lars.beuster
 */
@WebServlet(urlPatterns = "/*", loadOnStartup = 1)
public class TestServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Inject
	Domain domain;

	@Override
	public void init() {
		System.out.println("Servlet.init");
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.getWriter().println("GET request");
	}
}
