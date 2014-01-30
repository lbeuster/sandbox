package de.lbe.sandbox.tomcat.testapp;

import java.io.IOException;

import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

import org.apache.deltaspike.core.api.provider.BeanManagerProvider;

/**
 * @author lars.beuster
 */
@WebFilter(urlPatterns = "/*")
public class TestFilter implements Filter {

	@Inject
	BeanManager beanManager;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		System.out.println("INIT: " + beanManager);
		System.out.println("INIT: " + BeanManagerProvider.getInstance().getBeanManager());

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		System.out.println(beanManager);
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}
}
