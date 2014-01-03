package de.lbe.sandbox.deltaspike.security;

import javax.inject.Inject;

import org.junit.Test;

import de.lbe.sandbox.deltaspike.security.MyPermitAll;

/**
 * @author lars.beuster
 */
public class PermitAllAuthorizerTest extends AbstractSecurityTest {

	@Inject
	private PermitAllService service;

	/**
	 * 
	 */
	@Test
	public void testPermitAll() {
		service.permitAll();
	}

	/**
	 * 
	 */
	static class PermitAllService {

		@MyPermitAll
		void permitAll() {
			// no impl
		}
	}
}
