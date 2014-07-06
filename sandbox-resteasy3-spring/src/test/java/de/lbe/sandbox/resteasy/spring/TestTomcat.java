package de.lbe.sandbox.resteasy.spring;

import de.asideas.lib.commons.tomcat.embedded.EmbeddedTomcat;

/**
 * @author lars.beuster
 */
public class TestTomcat extends EmbeddedTomcat {

	private TestTomcat() {
		setWebappDir("src/main/webapp");
		// setExtraClassesDir("target/test-classes");
	}
}