package de.lbe.sandbox.spring.security;

import org.springframework.stereotype.Component;

import de.asideas.ipool.commons.lib.spring.security.HasAuthority;

@Component
// @PreAuthorize("hasRole('ROLE_USERd')")
// @PreAuthorize("denyAll")
public class TestService {

	// @Secured("ROLE_USER")
	// @PreAuthorize("hasRole('ROLE_USER')")
	@HasAuthority("TestAuthority")
	public void accessPermitted() {
		System.out.println("test");
	}

	@HasAuthority("InvalidTestAuthority")
	public void accessDenied() {
		System.out.println("test");
	}
}
