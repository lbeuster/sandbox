package net.dontdrinkandroot.example.angularrestspringsecurity.security;

import net.dontdrinkandroot.example.angularrestspringsecurity.WebSecurityConfig;
import net.dontdrinkandroot.example.angularrestspringsecurity.domain.SessionRepository;
import net.dontdrinkandroot.example.angularrestspringsecurity.domain.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class XAuthTokenAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private SessionRepository sessionRepository;

	@Autowired
	private UserRepository userRepository;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		return authenticate(((XAuthTokenAuthentication) authentication).getToken());
	}

	protected Authentication authenticate(String token) {
		String userName = sessionRepository.getUserBySessionId(token);
		UserDetails details = userRepository.getUserByName(userName);

		// if not found the token was invalid - we don't want to throw an exception but use the anonymous user instead
		if (details == null) {
			return new AnonymousAuthenticationToken("somethingToHash", WebSecurityConfig.ANONYMOUS_USERNAME, AuthorityUtils.createAuthorityList("anonymous"));
		}
		UserDetailsAuthentication auth = new UserDetailsAuthentication(details);
		return auth;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return XAuthTokenAuthentication.class.isAssignableFrom(authentication);
	}
}