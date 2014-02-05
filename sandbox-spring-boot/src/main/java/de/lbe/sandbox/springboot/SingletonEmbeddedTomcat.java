package de.lbe.sandbox.springboot;

import de.asideas.lib.commons.lang.ExceptionUtils;
import de.asideas.lib.commons.lang.reflect.ReflectionUtils;
import de.lbe.sandbox.springboot.EmbeddedTomcat;

/**
 * @author lbeuster
 */
public class SingletonEmbeddedTomcat {

	/**
	 * The singleton tomcat.
	 */
	private static EmbeddedTomcat tomcat = null;

	/**
     *
     */
	protected SingletonEmbeddedTomcat() {
	}

	/**
	 * Initializes and starts up tomcat if tomcat is not started yet.
	 */
	@SuppressWarnings({ "unchecked" })
	public static synchronized <T extends EmbeddedTomcat> T boot(Class<T> bootClass) {
		if (tomcat == null) {
			try {
				tomcat = ReflectionUtils.newPrivilegedInstance(bootClass);
				tomcat.setAutoStopAtJVMShutdown(true);
				tomcat.start();
			} catch (Exception ex) {
				throw ExceptionUtils.throwAsUncheckedException(ex);
			}
		}
		return (T) tomcat;
	}

	/**
     *
     */
	public static synchronized void stop() {
		if (tomcat != null) {
			tomcat.stop();
			tomcat = null;
		}
	}

	/**
     *
     */
	public static synchronized EmbeddedTomcat getTomcat() {
		return tomcat;
	}
}
