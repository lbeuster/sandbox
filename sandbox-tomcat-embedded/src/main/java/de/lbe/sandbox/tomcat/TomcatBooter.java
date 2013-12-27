package de.lbe.sandbox.tomcat;

import java.io.File;

import javax.enterprise.inject.spi.BeanManager;
import javax.servlet.ServletException;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.Wrapper;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.deploy.ApplicationListener;
import org.apache.catalina.deploy.ContextResource;
import org.apache.catalina.deploy.ContextTransaction;
import org.apache.catalina.startup.Tomcat;
import org.apache.naming.factory.BeanFactory;
import org.apache.naming.resources.VirtualDirContext;
import org.jboss.weld.resources.ManagerObjectFactory;

import com.atomikos.icatch.jta.UserTransactionFactory;
import com.atomikos.icatch.jta.UserTransactionManager;

import de.lbe.sandbox.tomcat.weld.WeldListener;

/**
 * @author lars.beuster
 */
public class TomcatBooter {

	private int httpPort = 8080;

	private Tomcat tomcat;

	private StandardContext webappContext;

	private String webappDir = ".";

	private String extraClassesDir;

	/**
	 * 
	 */
	public void start() throws ServletException {

		this.tomcat = initTomcat();
		this.webappContext = initContext(this.tomcat);
		initCDI(this.webappContext);

		// start
		try {
			this.tomcat.start();
		} catch (LifecycleException ex) {
			throw new ServletException("Could not start Tomcat: " + ex, ex);
		}
		if (!isWebappAvailable()) {
			throw new IllegalStateException("Webapp could not be started. See log files for more information.");
		}

		// wait for requests
		this.tomcat.getServer().await();
	}

	/**
	 * 
	 */
	protected Tomcat initTomcat() {
		Tomcat tomcat = new Tomcat();
		tomcat.setPort(this.httpPort);
		tomcat.enableNaming();
		return tomcat;
	}

	/**
	 * 
	 */
	protected StandardContext initContext(Tomcat tomcat) throws ServletException {

		// create the context
		StandardContext context = (StandardContext) tomcat.addWebapp("/", new File(this.webappDir).getAbsolutePath());

		// declare an alternate location for your "WEB-INF/classes" dir
		if (this.extraClassesDir != null) {
			File additionWebInfClasses = new File(extraClassesDir);
			VirtualDirContext resources = new VirtualDirContext();
			resources.setExtraResourcePaths("/WEB-INF/classes=" + additionWebInfClasses);
			context.setResources(resources);
		}
		return context;
	}

	/**
	 * 
	 */
	protected void initCDI(StandardContext context) {

		// the main listener
		ApplicationListener listener = new ApplicationListener(WeldListener.class.getName(), false);
		context.addApplicationListener(listener);

		// bind BM to JNDI
		bindBeanManagerToJNDI(context, true);
	}

	/**
	 * 
	 */
	private void bindBeanManagerToJNDI(StandardContext context, boolean useWorkaround) {

		if (useWorkaround) {
			Wrapper servlet = Tomcat.addServlet(context, BindBeanManagerServlet.class.getName(), new BindBeanManagerServlet(context));
			servlet.setLoadOnStartup(1);
		} else {
			// this should be the regular was but this doesn't work because this binds only to java:comp/env/BeanManager instead of java:comp/BeanManager
			ContextResource beanManager = new ContextResource();
			beanManager.setAuth("Container");
			beanManager.setType(BeanManager.class.getName());
			beanManager.setName("BeanManager");
			beanManager.setProperty("factory", ManagerObjectFactory.class.getName());
			context.getNamingResources().addResource(beanManager);
		}
	}

	public boolean isWebappAvailable() {
		return this.webappContext != null && this.webappContext.getState().isAvailable();
	}

	private void configureTransactions(StandardContext ctx) {
		// UserTransaction
		ContextTransaction tx = new ContextTransaction();
		tx.setProperty("factory", UserTransactionFactory.class.getName());
		ctx.getNamingResources().setTransaction(tx);

		// TX-Manager
		ContextResource txManager = new ContextResource();
		txManager.setAuth("Container");
		txManager.setType(UserTransactionManager.class.getName());
		txManager.setName("TransactionManager");
		txManager.setProperty("factory", BeanFactory.class.getName());
		ctx.getNamingResources().addResource(txManager);
	}

	private void configureCDI(StandardContext ctx) {
		ContextResource beanManager = new ContextResource();
		beanManager.setAuth("Container");
		beanManager.setType(BeanManager.class.getName());
		beanManager.setName("BeanManager");
		beanManager.setProperty("factory", ManagerObjectFactory.class.getName());
		ctx.getNamingResources().addResource(beanManager);
	}

	public void setWebappDir(String webappDir) {
		this.webappDir = webappDir;
	}

	public void setExtraClassesDir(String extraClassesDir) {
		this.extraClassesDir = extraClassesDir;
	}
}
