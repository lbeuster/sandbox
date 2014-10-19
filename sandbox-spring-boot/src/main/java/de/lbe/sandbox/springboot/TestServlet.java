package de.lbe.sandbox.springboot;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author lbeuster
 */
public class TestServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public TestServlet() {
		System.out.println("init");
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) {
		System.out.println(request.getRequestURL());
	}
}