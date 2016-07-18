package de.lbe.sandbox.spring.security;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * @author lbeuster
 */
@SuppressWarnings("serial")
public class Authentication1 extends AbstractAuthenticationToken {

	private final String username;

	public Authentication1(String username, String... roles) {
		super(Arrays.asList(roles).stream().map(role -> new SimpleGrantedAuthority(role)).collect(Collectors.toList()));
		this.username = username;
		setAuthenticated(true);
	}

	@Override
	public Object getCredentials() {
		return null;
	}

	@Override
	public Object getPrincipal() {
		return username;
	}
}
