package de.lbe.sandbox.spring.security;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;

import org.junit.Test;

import de.lbe.sandbox.spring.security.rest.TokenTransfer;
import de.lbe.sandbox.spring.security.rest.UserTransfer;

public class SimpleTest extends AbstractTest {

	/**
	 * 
	 */
	@Test
	public void testLogin() {

		// authenticate
		String token = login("admin", "admin");

		//
		UserTransfer user = this.client.path("/rest/main/me").header("X-Auth-Token", token).get().assertIsStatusOk().getEntity(UserTransfer.class);
		System.out.println(user);
	}

	/**
	 * 
	 */
	@Test
	public void testInvalidLogin() {

		String username = "invalid";
		String password = "admin";

		// authenticate
		Form form = new Form().param("username", username).param("password", password);
		this.client.path("/rest/main/authenticate").post(Entity.form(form)).assertIsStatusForbidden();
	}

	/**
	 * 
	 */
	@Test
	public void testAnonymousUser() {
		UserTransfer entity = this.client.path("/rest/main/me").get().assertIsStatusOk().getEntity(UserTransfer.class);
		assertEquals(WebSecurityConfig1.ANONYMOUS_USERNAME, entity.getName());
	}

	/**
	 * 
	 */
	@Test
	public void testMeWithInvalidAuthToken() {
		UserTransfer entity = this.client.path("/rest/main/me").header("X-Auth-Token", "invalid-token").get().assertIsStatusOk().getEntity(UserTransfer.class);
		assertEquals("invalid-token", entity.getName());
	}

	/**
	 * 
	 */
	@Test
	public void testMethodSecurity() {
		String token = login("admin", "admin");
		String entity = this.client.path("/rest/main/service1/param").header("X-Auth-Token", token).get().assertIsStatusOk().getEntityAsString();
		System.out.println(entity);
	}

	/**
	 * 
	 */
	@Test
	public void testInvalidMethodSecurity() {
		String token = login("test", "test");
		this.client.path("/rest/main/service2/param").header("X-Auth-Token", token).get().assertIsStatusForbidden();
	}

	/**
	 * 
	 */
	private String login(String username, String password) {
		Form form = new Form().param("username", username).param("password", password);
		return this.client.path("/rest/main/authenticate").post(Entity.form(form)).assertIsStatusOk().getEntity(TokenTransfer.class).getToken();
	}
}