package de.lbe.tomee.webapp;

import org.junit.runners.model.InitializationError;

import de.asideas.lib.commons.arquillian.ArquillianRunner;

/**
 * @author lars.beuster
 */
public class JerseyArquillianRunner extends ArquillianRunner {

	public JerseyArquillianRunner(Class<?> klass) throws InitializationError {
		super(klass);
	}

	static {
		System.setProperty("com.sun.jersey.server.impl.cdi.lookupExtensionInBeanManager", "true");
	}
}
