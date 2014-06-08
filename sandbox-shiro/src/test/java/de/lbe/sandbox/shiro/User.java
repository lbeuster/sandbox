package de.lbe.sandbox.shiro;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author lbeuster
 */
public class User {

	public static final String ROLE_ADMIN = "admin";
	public static final String PERMISSION_TEST = "test.permission";

	public static final User ADMIN = new User("admin", Arrays.asList(ROLE_ADMIN)); 

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