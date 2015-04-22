package de.lbe.sandbox.spring.security;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;

import org.junit.Test;

import de.lbe.sandbox.spring.security.rest.TokenTransfer;
import de.lbe.sandbox.spring.security.rest.UserTransfer;
import de.lbe.sandbox.spring.security.security.WebSecurityConfigurer;

public class SimpleTest extends AbstractTest {

	/**
	 *
	 */
	@Test
	public void testStartup() {
	}

	/**
	 *
	 */
	@Test
	public void testPing() {
		String response = this.restClient.path("/api/main").get().assertIsStatusOk().getEntityAsString();
		assertEquals("pong", response);
	}

	/**
	 *
	 */
	@Test
	public void testLogin() {

		// authenticate
		String token = login("admin", "admin");

		//
		UserTransfer user = this.restClient.path("/rest/main/me").header("X-Auth-Token", token).get().assertIsStatusOk().getEntity(UserTransfer.class);
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
		this.restClient.path("/rest/main/authenticate").post(Entity.form(form)).assertIsStatusForbidden();
	}

	/**
	 *
	 */
	@Test
	public void testAnonymousUser() {
		UserTransfer entity = this.restClient.path("/api/main/me").get().assertIsStatusOk().getEntity(UserTransfer.class);
		assertEquals(WebSecurityConfigurer.ANONYMOUS_USERNAME, entity.getName());
	}

	/**
	 *
	 */
	@Test
	public void testMeWithInvalidAuthToken() {
		UserTransfer entity = this.restClient.path("/api/main/me").header("X-Auth-Token", "invalid-token").get().assertIsStatusOk().getEntity(UserTransfer.class);
		assertEquals("invalid-token", entity.getName());
	}

	/**
	 *
	 */
	@Test
	public void testMethodSecurity() {
		String token = login("admin", "admin");
		String entity = this.restClient.path("/api/main/service1/param").header("X-Auth-Token", token).get().assertIsStatusOk().getEntityAsString();
		System.out.println(entity);
	}

	/**
	 *
	 */
	@Test
	public void testInvalidMethodSecurity() {
		String token = login("test", "test");
		this.restClient.path("/api/main/service2/param").header("X-Auth-Token", token).get().assertIsStatusForbidden();
	}

	/**
	 *
	 */
	private String login(String username, String password) {
		Form form = new Form().param("username", username).param("password", password);
		return this.restClient.path("/api/main/authenticate").post(Entity.form(form)).assertIsStatusOk().getEntity(TokenTransfer.class).getToken();
	}
}