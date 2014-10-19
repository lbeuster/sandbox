package de.lbe.sandbox.springboot.old;

import org.springframework.stereotype.Component;

import de.asideas.lib.commons.tomcat.embedded.EmbeddedTomcat;
import de.asideas.lib.commons.util.convert.TypeConverter;

/**
 * <p>
 * Boots Tomcat.
 * </p>
 * <p>
 * This class has access to all classes.
 * </p>
 * 
 * @author lbeuster
 */
@Component
public class EmbeddedTomcatFactory {

	public static final String PROPERTY_HTTP_PORT = "httpPort";

	public static final String ENV_HTTP_PORT = "PORT";

	private Class<? extends EmbeddedTomcat> embeddedTomcatClass = EmbeddedTomcat.class;

	/**
	 * 
	 */
	public EmbeddedTomcat create() {

		EmbeddedTomcat tomcat = createEmbeddedTomcat();

		// port
		Integer port = getExplicitHttpPort();
		if (port != null) {
			tomcat.setPort(port.intValue());
		}
		return tomcat;
	}

	/**
	 * 
	 */
	protected EmbeddedTomcat createEmbeddedTomcat() {
		try {
			return this.embeddedTomcatClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException("Failed to instantiate embedded Tomcat: " + this.embeddedTomcatClass, e);
		}
	}

	/**
	 * 
	 */
	private Integer getExplicitHttpPort() {

		// look in system properties
		String value = System.getProperty(PROPERTY_HTTP_PORT);
		if (value != null) {
			return TypeConverter.toINT(value);
		}

		// look in environment
		value = System.getenv(ENV_HTTP_PORT);
		if (value != null) {
			return TypeConverter.toINT(value);
		}

		// no explicit port
		return null;
	}

	public void setEmbeddedTomcatClass(Class<? extends EmbeddedTomcat> embeddedTomcatClass) {
		this.embeddedTomcatClass = embeddedTomcatClass;
	}

	/**
	 * This method is mainly useful in tests.
	 */
	public static void prepareHttpPort(int port) {
		System.setProperty(PROPERTY_HTTP_PORT, String.valueOf(port));
	}
}
