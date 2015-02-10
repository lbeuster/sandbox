package de.lbe.sandbox.springboot.jersey1;

import org.junit.Test;

import com.sun.jersey.api.client.ClientResponse;

/**
 * @author lbeuster
 */
public class StartupTest extends AbstractSpringBootJersey1Test {

	@Test
	public void testRestResource() throws Exception {
		String message = target("/api/hello").get(String.class);
		assertEquals("HALLO", message);
	}

	@Test
	public void testExceptionMapper() throws Exception {
		String message = target("/api/hello/exception").get(ClientResponse.class).getEntity(String.class);
		assertNotNull(DefaultExceptionMapper.ENTITY, message);
	}

	@Test
	public void testAdmin() throws Exception {
		String message = target("/admin/beans").get(String.class);
		assertNotNull(message);
	}

	@Test
	public void testStaticContent() throws Exception {
		String message = target("/lars.txt").get(String.class);
		assertNotNull(message);
	}
}