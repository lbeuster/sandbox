package de.lbe.sandbox.deltaspike.security;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.junit.Test;
import org.picketlink.Identity;
import org.picketlink.idm.IdentityManager;

/**
 * @author lars.beuster
 */
public class ParameterTest extends AbstractSecurityTest {

	@Inject
	private TestService service;

	@Inject
	private Identity id;

	@Inject
	private IdentityManager idm;

	/**
	 * 
	 */
	@Test
	public void testAccessParam() {
		service.service("test");
	}

	/**
	 * 
	 */
	@RequestScoped
	static class TestService {

		@Employee
		String service(String param) {
			return param;
		}
	}

}
