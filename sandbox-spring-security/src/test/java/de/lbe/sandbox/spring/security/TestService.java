package de.lbe.sandbox.spring.security;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

@Component
public class TestService {

	@Secured("ROLE_USER")
	// @PreAuthorize("isAnonymous()")
	public void test() {
		System.out.println("test");
	}
}
