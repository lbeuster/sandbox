package net.dontdrinkandroot.example.angularrestspringsecurity.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationService {

	@Autowired
	private UserRepository userRepository;

	public UserDetails authenticate(String username, String password) {
		UserDetails details = userRepository.getUserByName(username);
		if (details == null) {
			throw new UsernameNotFoundException(username);
		}
		if (!password.equals(details.getPassword())) {
			throw new BadCredentialsException(username + ":" + password);
		}
		return details;
	}

}