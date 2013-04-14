package de.lbe.sandbox.openejb;

import java.util.Properties;

import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;

/**
 * @author lars.beuster
 */
public class OpenEJBContainer {

	private static EJBContainer ejbContainer = null;

	static {
		// System.setProperty("openejb.logger.external", "true");

	}

	/**
	 * 
	 */
	public static void restart() {
		stop();
		start();
	}

	/**
	 * 
	 */
	public static synchronized EJBContainer start() {
		if (ejbContainer == null) {
			System.setProperty("openejb.log.factory", "slf4j");
			
			Properties properties = new Properties();
			properties.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.openejb.core.LocalInitialContextFactory");
			properties.setProperty("openejb.embedded.remotable", "true");
			properties.setProperty("openejb.strict.interface.declaration", "true");
			properties.setProperty("openejb.validation.skip", "false");
			properties.setProperty("openejb.validation.output.level", "VERBOSE");

			// properties.setProperty("log4j.rootLogger", "fatal,C");
			// properties.setProperty("log4j.category.OpenEJB", "warn");
			// properties.setProperty("log4j.category.OpenEJB.options", "warn");
			// properties.setProperty("log4j.category.OpenEJB.server", "warn");
			// properties.setProperty("log4j.category.OpenEJB.startup", "warn");
			// properties.setProperty("log4j.category.OpenEJB.startup.service", "warn");
			// properties.setProperty("log4j.category.OpenEJB.startup.config", "warn");
			// properties.setProperty("log4j.category.OpenWebBeans.startup.config", "warn");

			properties.putAll(defineHsqldbDataSource("LanguageTX"));

			properties.setProperty("Language.hibernate.show_sql", "true");
			properties.setProperty("Language.hibernate.format_sql", "false");

			ejbContainer = EJBContainer.createEJBContainer(properties);
		}
		return ejbContainer;
	}

	public static Properties defineHsqldbDataSource(String dataSourceName) {
		Properties properties = new Properties();
		properties.setProperty(dataSourceName, "new://Resource?type=DataSource");
		properties.setProperty(dataSourceName + ".JdbcDriver", "org.hsqldb.jdbcDriver");
		properties.setProperty(dataSourceName + ".JdbcUrl", "jdbc:hsqldb:mem:" + dataSourceName + ";shutdown=false");
		properties.setProperty(dataSourceName + ".JtaManaged", "true");
		return properties;
	}

	/**
	 * 
	 */
	public static synchronized void stop() {
		if (ejbContainer != null) {
			try {
				ejbContainer.close();
			} finally {
				ejbContainer = null;
			}
		}
	}

	/**
	 * 
	 */
	public static EJBContainer get() {
		return start();
	}

	/**
	 * 
	 */
	public static Context getContext() {
		return get().getContext();
	}
}
