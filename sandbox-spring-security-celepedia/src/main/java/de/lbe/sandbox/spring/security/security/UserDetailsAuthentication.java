package de.lbe.sandbox.spring.security.security;

import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserDetailsAuthentication implements Authentication {

	private static final long serialVersionUID = 1L;

	private final UserDetails user;

	private boolean authenticated = false;

	public UserDetailsAuthentication(UserDetails user) {
		this.user = user;
		this.authenticated = true;
	}

	@Override
	public String getName() {
		return this.user.getUsername();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.user.getAuthorities();
	}

	@Override
	public Object getCredentials() {
		return this.user.getPassword();
	}

	@Override
	public Object getDetails() {
		return this.user;
	}

	@Override
	public Object getPrincipal() {
		return this.user;
	}

	@Override
	public boolean isAuthenticated() {
		return this.authenticated;
	}

	@Override
	public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
		this.authenticated = isAuthenticated;
	}
}