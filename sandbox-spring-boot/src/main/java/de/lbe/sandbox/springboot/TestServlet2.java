package de.lbe.sandbox.springboot;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.LoggerFactory;

/**
 * @author lbeuster
 */
public class TestServlet2 extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public TestServlet2() {
		System.out.println("init2");
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) {
		LoggerFactory.getLogger(getClass()).info("Incoming request: {}", request.getRequestURL());
	}
}