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
	public void testInectionAwareValidator() throws Exception {
		client.path("api/test/injectionAwareValidation").query("param", "value").get().assertIsStatusOk();
	}

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
	public void testMethodValidationWithValid() throws Exception {
		TestPojo pojo = new TestPojo();
		pojo.setMessage(null);
		client.path("api/test/pojoMethodValidation").postJSON(pojo).assertIsStatusBadRequest();
	}

	/**
	 * 
	 */
	@Test
	public void testMetrics() throws Exception {
		client.path("api/test/metrics").get().assertIsStatusOk();
	}

	/**
	 * 
	 */
	@Test
	public void testHealthCheck() throws Exception {
		client.path("api/test/healthcheck").get().assertIsStatusOk();
	}
}
