package de.lbe.sandbox.tomcat;

import java.io.IOException;

import javax.enterprise.inject.Vetoed;
import javax.servlet.ServletException;

import org.apache.catalina.Wrapper;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;

import de.asideas.lib.commons.tomcat.embedded.EmbeddedTomcat;

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
public class EmbeddedWeldTomcat extends EmbeddedTomcat {

	/**
	 * 
	 */
	@Override
	protected void initContext() throws ServletException, IOException {
		super.initContext();
		initCDI();
	}

	/**
	 *
	 */
	protected void initCDI() {

		// bind BM to JNDI
		bindBeanManagerToJNDI(true);
	}

	/**
	 *
	 */
	private void bindBeanManagerToJNDI(boolean useWorkaround) {

		if (useWorkaround) {
			StandardContext webappContext = getWebappContext();
			Wrapper servlet = Tomcat.addServlet(webappContext, BindBeanManagerServlet.class.getName(), new BindBeanManagerServlet(webappContext));
			servlet.setLoadOnStartup(1);
		} else {
			// this should be the regular way but this doesn't work because this binds only to java:comp/env/BeanManager instead of java:comp/BeanManager
			// ContextResource beanManager = new ContextResource();
			// beanManager.setAuth("Container");
			// beanManager.setType(BeanManager.class.getName());
			// beanManager.setName("BeanManager");
			// beanManager.setProperty("factory", ManagerObjectFactory.class.getName());
			// context.getNamingResources().addResource(beanManager);
		}
	}
}