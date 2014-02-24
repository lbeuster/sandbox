package de.lbe.sandbox.tomcat;

/**
 * @author lars.beuster
 */
public class TestTomcat extends EmbeddedTomcat {

	private TestTomcat() {
		setWebappDir("src/main/webapp");
		setExtraClassesDir("target/test-classes");
	}

	public static TestTomcat boot() {
		return SingletonEmbeddedTomcat.boot(TestTomcat.class);
	}
}