package de.lbe.sandbox.spring.data;

import java.util.List;

import org.springframework.data.repository.Repository;

public interface AutoUserRepository extends Repository<User, String> {

	public void save(User user);

	public void delete(User user);

	public User findByUsername(String username);

	public List<User> findAll();
}