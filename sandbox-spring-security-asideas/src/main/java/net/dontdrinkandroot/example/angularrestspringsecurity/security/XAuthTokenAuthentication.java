package net.dontdrinkandroot.example.angularrestspringsecurity.security;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

public class XAuthTokenAuthentication implements Authentication {

	private static final long serialVersionUID = 1L;

	private final String token;

	private boolean authenticated = false;

	public XAuthTokenAuthentication(String token) {
		this.token = token;
	}

	public String getToken() {
		return this.token;
	}

	@Override
	public String getName() {
		return this.token;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.EMPTY_LIST;
	}

	@Override
	public Object getCredentials() {
		return this.token;
	}

	@Override
	public Object getDetails() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getPrincipal() {
		return this.token;
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