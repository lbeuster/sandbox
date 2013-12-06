package de.lbe.sandbox.tomcat;

import javax.servlet.ServletException;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

import de.asideas.lib.commons.io.FileUtils;
import de.asideas.lib.commons.lang.Assert;
import de.asideas.lib.commons.lang.StringUtils;
import de.asideas.lib.commons.lang.XRunnable;

/**
 * Meant as an embedded tomcat for integration tests.
 * 
 * @author Lars Beuster
 */
public class EmbeddedTestTomcat extends Tomcat {

	public static final String DEFAULT_WEBAPP_DIR = "src/main/webapp";

	/**
	 * We don't use 8080 to not interfer with regular servlet containers.
	 */
	public static final int DEFAULT_HTTP_PORT = 8081;

	public static final String DEFAULT_HOSTNAME = "0.0.0.0";

	public static final String DEFAULT_CONTEXT_PATH = "/";

	/**
     *
     */
	public static final String DEFAULT_CATALINA_BASE = "target/catalina.base";

	/**
     *
     */
	private String webappDir = DEFAULT_WEBAPP_DIR;

	/**
     *
     */
	private String contextPath = DEFAULT_CONTEXT_PATH;

	/**
     *
     */
	private Context webappContext = null;

	/**
     *
     */
	private boolean useSurefireJarScanner = true;

	/**
	 * Should we add a shutdown-hook to stop Jetty at shutdown-time of the JVM.
	 */
	private boolean autoStopAtJVMShutdown = false;

	/**
	 * Per default stop() doesn't free the ports - but we want to free the ports if we call stop().
	 */
	private boolean stopDestroysTomcat = true;

	/**
     *
     */
	public EmbeddedTestTomcat() {
		setPort(DEFAULT_HTTP_PORT);
		setBaseDir(DEFAULT_CATALINA_BASE);
		setHostname(DEFAULT_HOSTNAME);
	}

	/**
     *
     */
	@Override
	public void start() throws LifecycleException {

		// create + add the context
		try {
			this.webappContext = addWebapp();
		} catch (ServletException ex) {
			throw new LifecycleException(ex);
		}

		super.start();

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

		// add taglib support in maven tests
		SurefireJarScanner.install(this.webappContext.getServletContext());

		// check that our webapp is started without any errors
		assertWebappIsAvailable();
	}

	/**
     *
     */
	@Override
	public void stop() throws LifecycleException {
		super.stop();
		if (this.stopDestroysTomcat) {
			destroy();
		}
	}

	/**
     *
     */
	public void join() {
		this.getServer().await();
	}

	/**
     *
     */
	protected void assertWebappIsAvailable() {
		Assert.isTrue(isWebappAvailable(), "webapp has not been started");
	}

	/**
	 * Unfortunately tomcat creates + adds the context by default, so we cannot intercept the creating to create our own stuff and then add it.
	 * 
	 * @throws ServletException
	 */
	protected Context addWebapp() throws ServletException {

		// only for safety, perhaps not really necessary
		this.webappDir = FileUtils.toAbsoluteFileName(this.webappDir);

		// create + configure the context
		Context context = addWebapp(this.contextPath, this.webappDir);
		// ((StandardJarScanner) context.getJarScanner()).setScanAllDirectories(true);

		// create a fix for maven-surefire-execution
		if (this.useSurefireJarScanner) {
			context.setJarScanner(new SurefireJarScanner());
		}
		return context;
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
		String contextPath = this.webappContext != null ? this.webappContext.getPath() : this.contextPath;
		if (StringUtils.isNotEmpty(contextPath) && !"/".equals(contextPath)) {
			url.append(contextPath);
		}
		return url.toString();
	}

	public String getHostname() {
		return this.hostname;
	}

	public final void setHttpConnectorPort(int port) {
		setPort(port);
	}

	public int getHttpConnectorPort() {
		return getPort();
	}

	public int getPort() {
		return this.port;
	}

	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}

	public String getContextPath() {
		return this.contextPath;
	}

	public void setWebappDir(String webappDir) {
		this.webappDir = webappDir;
	}

	public boolean isWebappAvailable() {
		return this.webappContext != null && this.webappContext.getState().isAvailable();
	}

	public void setAutoStopAtJVMShutdown(boolean autoStopAtJVMShutdown) {
		this.autoStopAtJVMShutdown = autoStopAtJVMShutdown;
	}

	public void setUseSurefireJarScanner(boolean useSurefireJarScanner) {
		this.useSurefireJarScanner = useSurefireJarScanner;
	}

	public void setStopDestroysTomcat(boolean stopDestroysTomcat) {
		this.stopDestroysTomcat = stopDestroysTomcat;
	}
}
