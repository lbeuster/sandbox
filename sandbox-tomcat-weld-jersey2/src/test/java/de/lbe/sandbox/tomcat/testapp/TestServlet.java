package de.lbe.sandbox.tomcat.testapp;

import java.io.IOException;

import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import com.atomikos.icatch.jta.UserTransactionImp;

/**
 * @author lars.beuster
 */
@WebServlet(urlPatterns = "/test/*", loadOnStartup = 1)
public class TestServlet extends HttpServlet {

	@Inject
	BeanManager beanManager;

	// @Resource
	UserTransaction tx;

	@Override
	public void init() {
		System.out.println("Servlet.init: " + beanManager);

	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		try {
			UserTransaction tx = new UserTransactionImp();
			tx.begin();
			resp.getWriter().write("HALLO: " + tx);
			tx.commit();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
}