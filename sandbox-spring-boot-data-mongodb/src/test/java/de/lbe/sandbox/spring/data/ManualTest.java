package de.lbe.sandbox.spring.data;

import static org.hamcrest.Matchers.hasSize;

import java.time.ZonedDateTime;
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
		User user = new User(getTestMethodName(), username, "mypassword", null);

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
	public void testZonedDateTime() {

		String username = getTestMethodName();
		ZonedDateTime now = ZonedDateTime.now();
		User user = new User(getTestMethodName(), username, "mypassword", now);

		// save
		userRepository.save(user);

		// find the saved user again.
		User savedUser = userRepository.findByUsername(username);
		assertNotNull(savedUser);
		assertEquals(now, savedUser.getZonedDateTime());
	}

	/**
	 *
	 */
	@Test
	public void testClassNameInDatabase() {

		String username = "myusername";

		User user = new User(getTestMethodName(), username, "mypassword", null);

		// save
		userRepository.save(user);

		//
		Query searchUserQuery = new Query(Criteria.where("username").is(username));
		OtherUser otherUser = mongoOperations.findOne(searchUserQuery, OtherUser.class);
		assertNotNull(otherUser);
	}
}