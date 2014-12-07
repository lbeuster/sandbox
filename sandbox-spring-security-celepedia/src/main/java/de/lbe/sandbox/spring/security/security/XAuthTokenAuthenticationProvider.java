package de.lbe.sandbox.spring.security.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import de.lbe.sandbox.spring.security.domain.SessionRepository;
import de.lbe.sandbox.spring.security.domain.UserRepository;

@Component
public class XAuthTokenAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private SessionRepository sessionRepository;

	@Autowired
	private UserRepository userRepository;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		XAuthTokenAuthentication xAuthentication = (XAuthTokenAuthentication) authentication;
		String token = xAuthentication.getToken();
		String userName = sessionRepository.getUserBySessionId(token);
		UserDetails details = userRepository.getUserByName(userName);

		// if not found the token was invalid - we don't want to throw an exception but use the anonymous user instead
		if (details == null) {
			return authentication;
			//
			// return new AnonymousAuthenticationToken("somethingToHash", WebSecurityConfig.ANONYMOUS_USERNAME, AuthorityUtils.createAuthorityList("anonymous"));
		}
		UserDetailsAuthentication auth = new UserDetailsAuthentication(details);
		return auth;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return XAuthTokenAuthentication.class.isAssignableFrom(authentication);
	}
}