package de.lbe.sandbox.springboot;

import de.asideas.lib.commons.lang.ClassLoaderUtils;
import de.asideas.lib.commons.util.logging.slf4j.SLF4JUtils;

/**
 * @author lars.beuster
 */
public class Main {

	public static void main(String[] args) {
		new Main().start(true);
	}

	public void start(boolean await) {
		System.out.println(ClassLoaderUtils.toString(Thread.currentThread().getContextClassLoader()));
		SLF4JUtils.installJulToSlf4jBridge();
		EmbeddedTomcat tomcat = new EmbeddedTomcat();
		// tomcat.setWebappDir("src/main/webapp");
		tomcat.setPort(1234);
		tomcat.start();
		if (await) {
			tomcat.await();
		}
	}
}
