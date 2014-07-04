package de.lbe.sandbox.springboot;

import javax.enterprise.inject.Vetoed;
import javax.enterprise.inject.spi.CDI;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.catalina.core.StandardContext;
import org.apache.naming.ContextAccessController;

import de.asideas.lib.commons.lang.Assert;

/**
 * <p>
 * Why do we need this class? Jersey 2 (CdiComponentProvider from org.glassfish.jersey.containers.glassfish:jersey-gf-cdi:2.4.1) looks up BeanManager from JNDI (standard conform).
 * Unfortunatly it's not possible with normal Tomcat config to bind the BM to the required name java:comp/BeanManager. Tomcat can only bind to java:conp/env/BeanManager - which is
 * useless. That's why we have to bind ourselves.
 * </p>
 * <p>
 * Why do we use a servlet? Because we have to pass the StandardContext and we cannot configure Tomcat with an instance of a listener - only the class handle or name.
 * </p>
 * 
 * @author lbeuster
 */
@Vetoed
public class BindBeanManagerServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private final StandardContext webappContext;

	/**
	 * 
	 */
	public BindBeanManagerServlet(StandardContext webappContext) {
		Assert.isNotNull(webappContext, "webappContext");
		this.webappContext = webappContext;
	}

	/**
	 * 
	 */
	@Override
	public void init() throws ServletException {

		super.init();

		String name = this.webappContext.getNamingContextListener().getName();

		// the JNDI tree is readonly by default
		ContextAccessController.setWritable(name, this.webappContext);
		try {
			new InitialContext().bind("java:comp/BeanManager", CDI.current().getBeanManager());
		} catch (NamingException e) {
			throw new ServletException(e);
		} finally {
			ContextAccessController.setReadOnly(name);
		}
	}
}