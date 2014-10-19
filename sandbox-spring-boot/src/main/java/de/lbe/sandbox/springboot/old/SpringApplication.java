package de.lbe.sandbox.springboot.old;

import java.io.Closeable;
import java.util.Objects;

import de.asideas.lib.commons.spring.SpringUtils;
import de.asideas.lib.commons.tomcat.embedded.EmbeddedTomcat;

/**
 * @author lbeuster
 */
public class SpringApplication implements Closeable {

	private SpringApplicationContext springContext;

	private final Class<?>[] applicationClasses;

	private Thread shutdownHook;

	private boolean autoStopAtJVMShutdown = false;

	/**
	 * 
	 */
	public static SpringApplication main(Class<?>... applicationClasses) throws Exception {
		SpringApplication application = new SpringApplication(applicationClasses);
		application.boot();
		return application;
	}

	/**
	 * 
	 */
	public SpringApplication(Class<?>... applicationClasses) {
		Objects.requireNonNull(applicationClasses, "applicationClasses");
		this.applicationClasses = applicationClasses;
	}

	/**
	 * 
	 */
	public void boot() throws Exception {
		installShutdownHook();
		initSpring();
	}

	/**
	 * 
	 */
	private void installShutdownHook() {
		this.shutdownHook = new Thread("Shutdown " + getClass().getName()) {
			@Override
			public void run() {
				removeShutdownHook();
				if (autoStopAtJVMShutdown) {
					close();
				}
			}
		};
		Runtime.getRuntime().addShutdownHook(this.shutdownHook);
	}

	/**
	 * 
	 */
	private void removeShutdownHook() {
		try {
			Runtime.getRuntime().removeShutdownHook(this.shutdownHook);
		} catch (IllegalStateException ex) {
			// shutdown in progress
		}
	}

	/**
	 * 
	 */
	@Override
	public void close() {
		removeShutdownHook();
		if (this.springContext != null) {
			this.springContext.close();
		}
	}

	/**
	 * 
	 */
	private void initSpring() {

		// first we have to boot Spring without Tomcat because otherwise we had synchronization deadlocks (while the Tomcat bean is initializing Tomcat itself starts another thread
		// to initialize the servlet filter beans, which access the application context is in static way)
		this.springContext = new SpringApplicationContext();
		this.springContext.register(this.applicationClasses);
		this.springContext.refresh();

		// boot Tomcat
		EmbeddedTomcatBooter booter = this.springContext.getBean(EmbeddedTomcatBooter.class);
		EmbeddedTomcat tomcat = booter.boot();

		// not necessary but for consistency (and for tests)
		SpringUtils.registerSingletonBean(this.springContext, tomcat);
	}

	public SpringApplication autoStopAtJVMShutdown(boolean autoStopAtJVMShutdown) {
		this.autoStopAtJVMShutdown = autoStopAtJVMShutdown;
		return this;
	}

	public SpringApplicationContext getSpringContext() {
		return springContext;
	}
}
