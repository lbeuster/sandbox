package de.lbe.sandbox.spring.security;

import org.junit.Test;

import de.asideas.lib.commons.test.restclient.RestTarget;

/**
 * @author lbeuster
 */
public class SecurityUseCasesIT extends AbstractTest {

	@Test
	public void testBeansWithoutAuth() throws Exception {
		this.restClient.path("/admin/beans").get().assertIsStatusUnauthorized();
	}

	@Test
	public void testBeansWithAuth() throws Exception {
		RestTarget<?> target = addBasicAuth(this.restClient.path("/admin/beans"));
		target.get().assertIsStatusOk();
	}

	@Test
	public void testResourceWithoutAuth() {
		this.restClient.path("/api/main").get().assertIsStatusUnauthorized();
	}

	@Test
	public void testResourceWithAuth() throws Exception {
		RestTarget<?> target = this.restClient.path("/api/main").basicAuth("user", "password");
		String response = target.get().assertIsStatusOk().getEntityAsString();
		assertEquals("pong", response);
	}
}
