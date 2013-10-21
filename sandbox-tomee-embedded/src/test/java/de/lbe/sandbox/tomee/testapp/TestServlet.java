package de.lbe.sandbox.tomee.testapp;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author lars.beuster
 */
@WebServlet(urlPatterns = "/test")
public class TestServlet extends HttpServlet {

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.getWriter().write("HALLO");
	}
}
