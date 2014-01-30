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
import org.apache.tomcat.util.scan.StandardJarScanner;
import org.jboss.weld.resources.ManagerObjectFactory;

import com.atomikos.icatch.jta.UserTransactionFactory;
import com.atomikos.icatch.jta.UserTransactionManager;

import de.asideas.lib.commons.lang.StringUtils;
import de.asideas.lib.commons.lang.XRunnable;

/**
 * @author lars.beuster
 */
public class EmbeddedTomcat {

	private Tomcat tomcat;

	private StandardContext webappContext;

	private String hostname = "0.0.0.0";

	private int httpPort = 8080;

	private String catalinaBase = "catalina.base";

	private String contextPath = "/";

	private String webappDir = ".";

	private String extraClassesDir;

	/**
	 * Should we add a shutdown-hook to stop Tomcat at shutdown-time of the JVM.
	 */
	private boolean autoStopAtJVMShutdown = false;

	/**
	 * Per default stop() doesn't free the ports - but we want to free the ports if we call stop().
	 */
	private boolean stopDestroysTomcat = true;

	private boolean started = false;

	/**
	 * 
	 */
	public void start() {

		if (isStarted()) {
			throw new IllegalStateException("Tomcat has already been started.");
		}
		this.started = true;

		// init Tomcat
		try {
			this.tomcat = initTomcat();
			this.webappContext = initContext(this.tomcat);
		} catch (ServletException ex) {
			throw new RuntimeException("Could not create webapp context: " + ex, ex);
		}

		// init CDI
		initCDI(this.webappContext);

		// start Tomcat
		try {
			this.tomcat.start();
		} catch (LifecycleException ex) {
			throw new RuntimeException("Could not start Tomcat: " + ex, ex);
		}

		// add a JVM-shutdown-hook
		if (this.autoStopAtJVMShutdown) {
			Runnable runnable = new XRunnable<Void>(true) {

				@Override
				protected void runImpl() throws Exception {
					stop();
				}
			};
			Runtime.getRuntime().addShutdownHook(new Thread(runnable));
		}

		// check that the webapp could be started
		if (!isWebappAvailable()) {
			throw new IllegalStateException("Webapp could not be started. See log files for more information.");
		}
	}

	/**
	 * 
	 */
	public void await() {
		if (!isStarted()) {
			throw new IllegalStateException("Tomcat has not been started.");
		}
		this.tomcat.getServer().await();
	}

	/**
     *
     */
	public void stop() {
		if (!isStarted()) {
			throw new IllegalStateException("Tomcat has not been started.");
		}
		this.started = false;
		try {
			this.tomcat.stop();
			if (this.stopDestroysTomcat) {
				this.tomcat.destroy();
			}
		} catch (LifecycleException ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * 
	 */
	protected Tomcat initTomcat() {
		Tomcat tomcat = new Tomcat();
		tomcat.setPort(this.httpPort);
		tomcat.setHostname(this.hostname);
		tomcat.setBaseDir(this.catalinaBase);
		tomcat.enableNaming();
		return tomcat;
	}

	/**
	 * 
	 */
	protected StandardContext initContext(Tomcat tomcat) throws ServletException {

		// create the context
		String webappDir = new File(this.webappDir).getAbsolutePath();
		StandardContext context = (StandardContext) tomcat.addWebapp(getContextPath(), webappDir);

		// declare an alternate location for your "WEB-INF/classes" dir
		if (this.extraClassesDir != null) {
			File additionWebInfClasses = new File(extraClassesDir);
			VirtualDirContext resources = new VirtualDirContext();
			resources.setExtraResourcePaths("/WEB-INF/classes=" + additionWebInfClasses);
			context.setResources(resources);
		}

		((StandardJarScanner) context.getJarScanner()).setScanAllDirectories(true);

		// // create a fix for maven-surefire-execution
		// if (this.useSurefireJarScanner) {
		// context.setJarScanner(new SurefireJarScanner());
		// }
		// ((StandardJarScanner) context.getJarScanner()).setScanAllDirectories(true);

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

	/**
	 * @return The fully-qualified context-URL of the webapp without a "/" at the end.
	 */
	public String getWebappContextURL() {
		StringBuilder url = new StringBuilder();

		// protocol
		url.append("http://");

		// host
		url.append(getHostname()).append(":").append(getPort());

		// contextPath
		String contextPath = this.webappContext != null ? this.webappContext.getPath() : getContextPath();
		if (StringUtils.isNotEmpty(contextPath) && !"/".equals(contextPath)) {
			url.append(contextPath);
		}
		return url.toString();
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

	public boolean isStarted() {
		return this.started;
	}

	public void setWebappDir(String webappDir) {
		this.webappDir = webappDir;
	}

	public void setExtraClassesDir(String extraClassesDir) {
		this.extraClassesDir = extraClassesDir;
	}

	public void setAutoStopAtJVMShutdown(boolean autoStopAtJVMShutdown) {
		this.autoStopAtJVMShutdown = autoStopAtJVMShutdown;
	}

	public void setStopDestroysTomcat(boolean stopDestroysTomcat) {
		this.stopDestroysTomcat = stopDestroysTomcat;
	}

	public void setPort(int port) {
		this.httpPort = port;
	}

	public int getPort() {
		return this.httpPort;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getHostname() {
		return this.hostname;
	}

	public void setCatalinaBase(String catalinaBase) {
		this.catalinaBase = catalinaBase;
	}

	public String getContextPath() {
		return contextPath;
	}

	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}
}
