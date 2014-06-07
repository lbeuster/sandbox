package de.lbe.sandbox.shiro;

import java.util.Collection;

/**
 * @author lbeuster
 */
public class User {

	public static final String ROLE_ADMIN = "admin";

	private final String displayName;

	private final Collection<String> roles;

	public User(String displayName, Collection<String> roles) {
		this.displayName = displayName;
		this.roles = roles;
	}

	public String getDisplayName() {
		return this.displayName;
	}

	public Collection<String> getRoles() {
		return roles;
	}
}