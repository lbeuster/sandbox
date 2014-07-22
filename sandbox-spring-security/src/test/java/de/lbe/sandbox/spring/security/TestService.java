package de.lbe.sandbox.spring.security;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

@Component
// @PreAuthorize("hasRole('ROLE_USERd')")
public class TestService {

	// @Secured("ROLE_USER")
	@PreAuthorize("hasRole('ROLE_USER')")
	public void test() {
		System.out.println("test");
	}
}
