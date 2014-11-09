package de.lbe.sandbox.spring.data;

import java.util.List;

import javax.inject.Inject;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Component
public class UserRepository {

	@Inject
	private MongoOperations mongoOperations;

	public void save(User user) {
		this.mongoOperations.save(user);
	}

	public void remove(User user) {
		this.mongoOperations.remove(user);
	}

	public User findByUsername(String username) {
		Query searchUserQuery = new Query(Criteria.where("username").is(username));
		return mongoOperations.findOne(searchUserQuery, User.class);
	}

	public List<User> findAll() {
		return mongoOperations.findAll(User.class);
	}
}