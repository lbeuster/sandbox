package de.lbe.sandbox.shiro;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lbeuster
 */
public class UserRepository {

	private static final Map<String, User> USERS = new HashMap<String, User>();

	static {
		USERS.put("lars", new User("lars", Arrays.asList(User.ROLE_ADMIN)));
	}

	public User getUserByDisplayName(String displayName) {
		return this.USERS.get(displayName);
	}
}