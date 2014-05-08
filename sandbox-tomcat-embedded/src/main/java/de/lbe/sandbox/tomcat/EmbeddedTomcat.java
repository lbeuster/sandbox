package de.lbe.sandbox.tomcat;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.Valve;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.core.StandardHost;
import org.apache.catalina.startup.Tomcat;
import org.apache.commons.io.FileUtils;
import org.apache.naming.resources.VirtualDirContext;
import org.apache.tomcat.JarScanner;
import org.apache.tomcat.util.scan.StandardJarScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.asideas.lib.commons.lang.Assert;
import de.asideas.lib.commons.lang.ExceptionUtils;
import de.asideas.lib.commons.lang.StringUtils;
import de.asideas.lib.commons.lang.XRunnable;
import de.asideas.lib.commons.net.URLUtils;

/**
 * @author lars.beuster
 */
public class EmbeddedTomcat {

	private static final Logger LOGGER = LoggerFactory.getLogger(EmbeddedTomcat.class);

	private static boolean shutdownInProgress = false;

	private Tomcat tomcat;

	private StandardContext webappContext;

	private String hostname = "localhost";

	private File tmpDir;

	private int httpPort = 8080;

	private String catalinaBase;

	private String contextPath = "/";

	private String webappDir;

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

	private JarScanner jarScanner;

	private final List<Runnable> afterStopRunnables = new ArrayList<>();

	private static final String AUTH_ROLE = "developer";

	private static final String WEBXML_LOCATION = "src/test/resources/web.xml";

	/**
	 *
	 */
	public void start() {

		assertIsNotStarted();
		try {

			// init Tomcat
			initTomcat();
			this.webappContext = initContext(this.tomcat);

			// Hack for CEL-662, adding a user manually
			this.tomcat.addUser(AUTH_ROLE, "ideas987");
			this.tomcat.addRole(AUTH_ROLE, AUTH_ROLE);

			// start tomcat
			this.tomcat.start();
		} catch (RuntimeException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new RuntimeException("Could not create webapp context: " + ex, ex);
		}

		// check that the webapp could be started
		if (!isWebappAvailable()) {
			throw new IllegalStateException("Webapp could not be started. See log files for more information.");
		}
		this.started = true;

		// add a JVM-shutdown-hook only if we could start Tomcat (makes no sense to try to stop it if the start failed)
		if (this.autoStopAtJVMShutdown) {
			Runnable runnable = new XRunnable<Void>(true) {

				@Override
				protected void runImpl() throws Exception {
					if (isStarted()) {
						stop();
					}
				}
			};
			Runtime.getRuntime().addShutdownHook(new Thread(runnable));
		}

		// and at the end we add a hok to detect if we are in shutdown ode
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run() {
				shutdownInProgress = true;
			}
		}));
	}

	/**
	 *
	 */
	public void await() {
		assertIsStarted();
		this.tomcat.getServer().await();
	}

	/**
     *
     */
	public void stop() {
		assertIsStarted();
		this.started = false;

		try {
			this.tomcat.stop();
			if (this.stopDestroysTomcat) {
				this.tomcat.destroy();
			}
		} catch (LifecycleException ex) {
			throw new RuntimeException(ex);
		} finally {
			executeRunnables(this.afterStopRunnables);
		}
	}

	/**
	 *
	 */
	protected void initTomcat() throws Exception {
		adjustCatalinaBase();

		this.tomcat = new Tomcat();
		this.tomcat.setPort(this.httpPort);
		this.tomcat.setHostname(this.hostname);
		this.tomcat.setBaseDir(this.catalinaBase);
		this.tomcat.enableNaming();

		// per default tomcat uses the systemCL - but in case we run as an executable jar that's only the CL without the nested jars
		this.tomcat.getHost().setParentClassLoader(Thread.currentThread().getContextClassLoader());
	}

	/**
	 *
	 */
	private void adjustCatalinaBase() throws IOException {
		if (this.catalinaBase == null) {
			this.catalinaBase = tmpDir("catalina.base").getAbsolutePath();
		}
	}

	/**
	 *
	 */
	protected StandardContext initContext(Tomcat tomcat) throws ServletException, IOException {

		// create the context
		adjustWebappDir();
		StandardContext context = (StandardContext) tomcat.addWebapp(getContextPath(), this.webappDir);

		// declare an alternate location for your "WEB-INF/classes" dir
		if (this.extraClassesDir != null) {
			File additionWebInfClasses = new File(this.extraClassesDir);
			VirtualDirContext resources = new VirtualDirContext();
			resources.setExtraResourcePaths("/WEB-INF/classes=" + additionWebInfClasses);
			context.setResources(resources);
		}

		// create a fix for maven-surefire-execution
		if (this.jarScanner != null) {
			context.setJarScanner(this.jarScanner);
		}

		// we want Tomcat to scan all classpath resources (not only jars but even exploded jars - important for IDE development) for servlet annotations
		((StandardJarScanner) context.getJarScanner()).setScanAllDirectories(true);

		// add web.xml
		if (new File(WEBXML_LOCATION).exists()) {
			context.setAltDDName(WEBXML_LOCATION);
		}

		return context;
	}

	/**
	 *
	 */
	private void adjustWebappDir() throws IOException {
		if (this.webappDir == null) {
			this.webappDir = tmpDir("webapp").getAbsolutePath();
		}
	}

	/**
	 * @return The fully-qualified context-URL of the webapp without a "/" at the end.
	 */
	public URL getWebappContextURL() {
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
		return URLUtils.createURL(url.toString());
	}

	/**
	 * 
	 */
	protected File tmpDir(String path) throws IOException {

		// ensure that the root tmp dir exists
		if (this.tmpDir == null) {

			// create the tmp dir
			this.tmpDir = Files.createTempDirectory("embedded-tomcat").toAbsolutePath().toFile();

			// ensure that the tmp dir is deleted on shutdown
			this.afterStopRunnables.add(new Runnable() {
				@Override
				public void run() {
					FileUtils.deleteQuietly(EmbeddedTomcat.this.tmpDir);
				}
			});
		}

		// create a subdir
		File tmpSubDir = new File(this.tmpDir, path);
		FileUtils.forceMkdir(tmpSubDir);
		return tmpSubDir;
	}

	/**
	 * 
	 */
	private void executeRunnables(List<Runnable> runnables) {
		for (Runnable runnable : runnables) {
			try {
				runnable.run();
			} catch (Throwable ex) {
				ex.printStackTrace();
				LOGGER.warn("ignoring exception", ex);
				ExceptionUtils.rethrowThreadDeath(ex);
			}
		}
	}

	public boolean isWebappAvailable() {
		return this.webappContext != null && this.webappContext.getState().isAvailable();
	}

	public boolean isStarted() {
		return this.started;
	}

	protected void assertIsStarted() {
		Assert.isTrue(isStarted(), "Tomcat has not been started yet.");
	}

	protected void assertIsNotStarted() {
		Assert.isFalse(isStarted(), "Tomcat has already beed started.");
	}

	public void setWebappDir(String webappDir) {
		assertIsNotStarted();
		this.webappDir = webappDir;
	}

	public void setExtraClassesDir(String extraClassesDir) {
		assertIsNotStarted();
		this.extraClassesDir = extraClassesDir;
	}

	public void setAutoStopAtJVMShutdown(boolean autoStopAtJVMShutdown) {
		assertIsNotStarted();
		this.autoStopAtJVMShutdown = autoStopAtJVMShutdown;
	}

	public void setStopDestroysTomcat(boolean stopDestroysTomcat) {
		this.stopDestroysTomcat = stopDestroysTomcat;
	}

	public void setPort(int port) {
		assertIsNotStarted();
		this.httpPort = port;
	}

	public int getPort() {
		return this.httpPort;
	}

	public void setHostname(String hostname) {
		assertIsNotStarted();
		this.hostname = hostname;
	}

	public String getHostname() {
		return this.hostname;
	}

	public void setCatalinaBase(String catalinaBase) {
		assertIsNotStarted();
		this.catalinaBase = catalinaBase;
	}

	public String getContextPath() {
		return this.contextPath;
	}

	public void setContextPath(String contextPath) {
		assertIsNotStarted();
		this.contextPath = contextPath;
	}

	public void setJarScanner(JarScanner jarScanner) {
		assertIsNotStarted();
		this.jarScanner = jarScanner;
	}

	public void addHostValve(Valve valve) {
		((StandardHost) tomcat.getHost()).addValve(valve);
	}

	public static boolean isShutdownInProgress() {
		return shutdownInProgress;
	}
}
