package de.lbe.sandbox.deltaspike.security;

import javax.inject.Inject;

import org.junit.Test;

/**
 * @author lars.beuster
 */
public class HasPermissionTest extends AbstractSecurityTest {

	@Inject
	private HasPermissionService service;

	/**
	 * 
	 */
	@Test
	public void testPermitAll() {
		service.execute();
	}

	/**
	 * 
	 */
	static class HasPermissionService {

		@HasPermission("test.permission")
		void execute() {
			// no impl
		}
	}
}
