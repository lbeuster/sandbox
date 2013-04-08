package de.lbe.sandbox.servlet30;

import javax.servlet.http.HttpServletResponse;

import org.junit.Test;

import com.zanox.lib.commons.test.util.TestUtils;

/**
 * Provides a Jetty start up for testing against Jetty throughout several test classes.
 * 
 * @author lars.beuster
 */
public class ResourceFromClasspathTomcatTest extends AbstractTomcatTest {

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
		if (TestUtils.isEclipseRunning()) {
			fail("This test doesn't succeed under Eclipse with Maven-workspace resolution "
				+ "because the required document isn't servered from the jar but from the target/classes directory of the dependent project.");
			return;
		}
		this.httpClient.GET("resource-from-jar.txt", HttpServletResponse.SC_OK);
	}
}
