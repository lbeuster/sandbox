package de.lbe.sandbox.spring.data;

import static org.hamcrest.Matchers.hasSize;

import java.util.List;

import javax.inject.Inject;

import org.junit.Test;

/**
 * @author lbeuster
 */
public class SimpleTest extends AbstractTest {

	@Inject
	private UserRepository userRepository;

	/**
	 *
	 */
	@Test
	public void testUserLifecycle() {

		String username = "myusername";

		User user = new User();
		user.setUsername(username);
		user.setPassword("mypassword");

		// save
		userRepository.save(user);

		// find the saved user again.
		User savedUser = userRepository.findByUsername(username);
		assertNotNull(savedUser);

		List<User> users = userRepository.findAll();
		assertThat(users, hasSize(1));

		// delete
		userRepository.remove(user);

		users = userRepository.findAll();
		assertThat(users, hasSize(0));
	}
}