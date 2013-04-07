package de.lbe.sandbox.jboss7;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.runners.model.InitializationError;

/**
 * We need this class only to set the system-properties.
 * 
 * @author lars.beuster
 */
public class JBossArquillian extends Arquillian {

	static {
		System.setProperty("jboss.home", JBoss.JBOSS_HOME);
		System.setProperty("java.util.logging.manager", "org.jboss.logmanager.LogManager");
		// System.setProperty("jboss.embedded.root", "e:/tmp/jboss720");
	}

	public JBossArquillian(Class<?> klass) throws InitializationError {
		super(klass);
	}
}
