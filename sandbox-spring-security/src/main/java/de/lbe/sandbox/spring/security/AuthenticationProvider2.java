package de.lbe.sandbox.spring.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * @author lbeuster
 */
public class AuthenticationProvider2 implements AuthenticationProvider {

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		UsernamePasswordAuthenticationToken userAndPass = (UsernamePasswordAuthenticationToken) authentication;
		if (userAndPass.getName().equals("user2") && userAndPass.getCredentials().equals("pass2")) {
			return new Authentication1(userAndPass.getName(), "role2");
		}
		return authentication;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
	}
}
