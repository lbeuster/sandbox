package net.dontdrinkandroot.example.angularrestspringsecurity.domain;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class UserRepository {

	public UserDetails getUserByName(String username) {
		if ("admin".equals(username) || "test".equals(username)) {
			List<GrantedAuthority> authorities = new ArrayList();
			authorities.add(new SimpleGrantedAuthority("ROLE_" + username + "-role"));
			authorities.add(new SimpleGrantedAuthority(username));
			return new User(username, username, authorities);
		}
		return null;
	}
}