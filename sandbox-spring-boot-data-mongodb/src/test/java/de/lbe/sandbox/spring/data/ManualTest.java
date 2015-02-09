package de.lbe.sandbox.spring.data;

import static org.hamcrest.Matchers.hasSize;

import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

/**
 * @author lbeuster
 */
public class ManualTest extends AbstractTest {

	@Inject
	private ManualUserRepository userRepository;

	@Inject
	private MongoOperations mongoOperations;

	/**
	 *
	 */
	@Test
	public void testUserLifecycle() {

		String username = "myusername";

		User user = new User(getTestMethodName(), username, "mypassword");

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

	/**
	 *
	 */
	@Test
	public void testClassNameInDatabase() {

		String username = "myusername";

		User user = new User(getTestMethodName(), username, "mypassword");

		// save
		userRepository.save(user);

		//
		Query searchUserQuery = new Query(Criteria.where("username").is(username));
		OtherUser otherUser = mongoOperations.findOne(searchUserQuery, OtherUser.class);
		assertNotNull(otherUser);
	}
}