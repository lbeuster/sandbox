package de.lbe.sandbox.tomcat;

import de.asideas.lib.commons.tomcat.embedded.EmbeddedTomcat;
import de.asideas.lib.commons.tomcat.embedded.test.SingletonEmbeddedTestTomcat;

/**
 * @author lars.beuster
 */
public class TestTomcat extends EmbeddedTomcat {

	private TestTomcat() {
		// setWebappDir("src/main/webapp");
		setExtraClassesDir("target/test-classes");
	}

	public static TestTomcat boot() {
		return SingletonEmbeddedTestTomcat.ensureIsBooted(TestTomcat.class);
	}
}