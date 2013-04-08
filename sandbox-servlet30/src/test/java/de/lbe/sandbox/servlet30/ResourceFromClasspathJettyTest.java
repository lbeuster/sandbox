package de.lbe.sandbox.servlet30;

import javax.servlet.http.HttpServletResponse;

import org.junit.Test;

/**
 * Provides a Jetty start up for testing against Jetty throughout several test classes.
 * 
 * @author lars.beuster
 */
public class ResourceFromClasspathJettyTest extends AbstractJettyTest {

	/**
	 * 
	 */
	@Test
	public void testWebInfClasses() throws Exception {
		this.httpClient.GET("resource-from-webinf-classes.txt", HttpServletResponse.SC_NOT_FOUND);
	}

	/**
	 * 
	 */
	@Test
	public void testJar() throws Exception {
		// should work according to servlet-spec but doesn't
		this.httpClient.GET("resource-from-jar.txt", HttpServletResponse.SC_NOT_FOUND);
	}
}
