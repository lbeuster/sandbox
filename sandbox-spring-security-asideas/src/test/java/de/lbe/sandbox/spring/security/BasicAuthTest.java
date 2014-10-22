package de.lbe.sandbox.spring.security;

import org.junit.Test;

public class BasicAuthTest extends AbstractTest {

	/**
	 * 
	 */
	@Test
	public void testLogin() {
		this.client.path("/rest/basic").basicAuth(WebSecurityConfig2.USERNAME, WebSecurityConfig2.PASSWORD).get().assertIsStatusOk();
	}

	/**
	 * 
	 */
	@Test
	public void testLoginDenied() {
		this.client.path("/rest/basic").get().assertIsStatusUnauthorized();
	}
}