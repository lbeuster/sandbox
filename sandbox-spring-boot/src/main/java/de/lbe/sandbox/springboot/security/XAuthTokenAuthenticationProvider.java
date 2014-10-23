package de.lbe.sandbox.springboot.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

public class XAuthTokenAuthenticationProvider implements AuthenticationProvider {

	private final SessionRepository sessionRepository;

	private final UserRepository userRepository;

	public XAuthTokenAuthenticationProvider(SessionRepository sessionRepository, UserRepository userRepository) {
		this.sessionRepository = sessionRepository;
		this.userRepository = userRepository;
	}

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