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
	public void testGET() throws Exception {
		String response = client.path("lars/rest").get().assertIsStatusOk().getEntityAsString();
		System.out.println(response);
	}
}
