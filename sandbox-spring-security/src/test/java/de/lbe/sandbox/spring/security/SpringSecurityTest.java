package de.lbe.sandbox.spring.security;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.asideas.lib.commons.test.junit.AbstractJUnit4Test;

/**
 * @author lars.beuster
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SecurityConfig.class)
public class SpringSecurityTest extends AbstractJUnit4Test {

	@Autowired
	private TestService service;

	@Autowired
	private SampleAuthenticationManager authManager;

	@Test
	public void testSecurity() throws Exception {

		String name = "user";
		String password = "user";

		Authentication request = new UsernamePasswordAuthenticationToken(name, password);
		Authentication result = authManager.authenticate(request);
		SecurityContextHolder.getContext().setAuthentication(result);

		service.test();
	}
}
