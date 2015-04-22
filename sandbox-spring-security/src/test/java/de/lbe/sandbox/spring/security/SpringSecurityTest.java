package de.lbe.sandbox.spring.security;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;

import de.asideas.ipool.commons.lib.spring.test.AbstractSpringIT;

/**
 * @author lars.beuster
 */
@ContextConfiguration(classes = SecurityConfig.class)
public class SpringSecurityTest extends AbstractSpringIT {

	@Autowired
	private TestService service;

	@Autowired
	private SampleAuthenticationManager authManager;

	@Before
	public void setUp() {
		String name = "user";
		String password = "user";

		Authentication request = new UsernamePasswordAuthenticationToken(name, password);
		Authentication result = authManager.authenticate(request);
		SecurityContextHolder.getContext().setAuthentication(result);
	}

	@Test
	public void testAccessPermitted() throws Exception {
		service.accessPermitted();
	}

	@Test(expected = AccessDeniedException.class)
	public void testAccessDenied() throws Exception {
		service.accessDenied();
	}
}
