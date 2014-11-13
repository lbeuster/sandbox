package de.lbe.sandbox.springboot.jersey2;

import org.junit.Test;

/**
 * @author lbeuster
 */
public class StartupTest extends AbstractSpringBootResteasyTest {

	@Test
	public void testRestResource() throws Exception {
		String message = target("/rest/hello").request().get(String.class);
		assertEquals("HALLO", message);
	}

	@Test
	public void testAdmin() throws Exception {
		String message = target("/admin/beans").request().get(String.class);
		assertNotNull(message);
	}
}