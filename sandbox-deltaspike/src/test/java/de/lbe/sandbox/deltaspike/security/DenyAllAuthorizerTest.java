package de.lbe.sandbox.deltaspike.security;

import javax.inject.Inject;

import org.junit.Test;

import de.lbe.sandbox.deltaspike.security.AccessDeniedException;
import de.lbe.sandbox.deltaspike.security.MyDenyAll;

/**
 * @author lars.beuster
 */
public class DenyAllAuthorizerTest extends AbstractSecurityTest {

	@Inject
	private DenyAllService service;

	/**
	 * 
	 */
	@Test(expected = AccessDeniedException.class)
	public void testDenyAll() {
		service.denyAll();
	}

	/**
	 * 
	 */
	static class DenyAllService {

		@MyDenyAll
		void denyAll() {
			// no impl
		}
	}
}
