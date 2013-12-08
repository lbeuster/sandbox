package de.lbe.sandbox.deltaspike.security;

/**
 * @author lars.beuster
 */
public class SecuredService {

	/**
	 * 
	 */
	@CustomSecurityBinding
	public void testRoleBasedSecurity() {
		System.out.println("CALL");
	}
}
