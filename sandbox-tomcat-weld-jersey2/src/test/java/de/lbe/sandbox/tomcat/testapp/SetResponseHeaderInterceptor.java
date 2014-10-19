package de.lbe.sandbox.tomcat.testapp;

import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.deltaspike.core.api.common.DeltaSpike;

/**
 * @author lbeuster
 */
@Interceptor
@SetResponseHeader
public class SetResponseHeaderInterceptor {

	public static final String HEADER_NAME = "HEADER";

	/**
	 * I found no other possibility to set a response header (other than ResponseBuilder).
	 */
	@Inject
	@DeltaSpike
	private HttpServletResponse response;

	@Inject
	private BeanManager beanManager;

	/**
	 * 
	 */
	@AroundInvoke
	public Object intercept(InvocationContext context) throws Exception {
		response.setHeader(HEADER_NAME, "value");
		return context.proceed();
	}
}