package de.lbe.sandbox.resteasy.spring;

import org.junit.Test;

/**
 * @author lars.beuster
 */
public class RestTest extends AbstractRestTest {

	/**
	 * 
	 */
	@Test
	public void testMethodValidation() throws Exception {
		client.path("api/test/methodValidation").get().assertIsStatusBadRequest();
	}

	/**
	 * 
	 */
	@Test
	public void testMetrics() throws Exception {
		client.path("api/test/metrics").get().assertIsStatusOk();
	}
}
