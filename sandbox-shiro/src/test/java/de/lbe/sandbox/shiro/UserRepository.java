package de.lbe.sandbox.shiro;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lbeuster
 */
public class UserRepository {

	private static final Map<String, User> users = new HashMap<>();

	static {
		add(User.ADMIN);
	}

	public static void add(User user) {
		users.put(user.getDisplayName(), user);
	}

	public User findByDisplayName(String displayName) {
		return users.get(displayName);
	}
}