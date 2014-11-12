package de.lbe.sandbox.springboot.resteasy;

import org.junit.Test;

/**
 * @author lbeuster
 */
public class StartupTest extends AbstractSpringBootResteasyTest {

	@Test
	public void testRestResource() throws Exception {
		String message = this.restClient.path("/rest/hello").get().assertIsStatusOk().getEntityAsString();
		assertEquals("HALLO", message);
	}

	@Test
	public void testAdmin() throws Exception {
		String message = this.restClient.path("/admin/beans").get().assertIsStatusOk().getEntityAsString();
		assertNotNull(message);
	}
}