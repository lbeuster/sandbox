package de.lbe.sandbox.deltaspike.security;

import javax.inject.Inject;

import org.apache.deltaspike.security.api.authorization.AccessDeniedException;
import org.junit.Test;

/**
 * @author lars.beuster
 */
public class AnnotationOnClassAndMethodTest extends AbstractSecurityTest {

	@Inject
	private TestService service;

	/**
	 * 
	 */
	@Test(expected = AccessDeniedException.class)
	public void testSecurity() {
		service.execute();
	}

	/**
	 * 
	 */
	@MyDenyAll
	static class TestService {

		@HasPermission("test.permission")
		void execute() {
			// no impl
		}
	}
}
