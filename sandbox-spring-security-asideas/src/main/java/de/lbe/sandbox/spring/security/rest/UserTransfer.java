package de.lbe.sandbox.spring.security.rest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;

import de.asideas.lib.commons.lang.ObjectUtils;

public class UserTransfer {

	private String name;

	private Collection<String> authorities;

	public UserTransfer(String userName, Collection<? extends GrantedAuthority> authorities) {
		this.setName(userName);
		this.setAuthorities(toAuthorities(authorities));
	}

	public UserTransfer() {
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private List<String> toAuthorities(Collection<? extends GrantedAuthority> authorities) {
		List<String> auths = new ArrayList<>();
		for (GrantedAuthority authority : authorities) {
			auths.add(authority.getAuthority());
		}
		return auths;
	}

	public Collection<String> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Collection<String> authorities) {
		this.authorities = authorities;
	}

	@Override
	public String toString() {
		return ObjectUtils.dumpViaReflection(this);
	}
}