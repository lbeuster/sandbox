package de.lbe.sandbox.spring.security.rest;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import de.lbe.sandbox.spring.security.domain.AuthenticationService;
import de.lbe.sandbox.spring.security.domain.SessionRepository;
import de.lbe.sandbox.spring.security.domain.TestApplicationService;
import de.lbe.sandbox.spring.security.security.UserDetailsAuthentication;

@Component
@Path("/main")
public class MainResource {

	@Autowired
	private AuthenticationService authenticationService;

	@Autowired
	private SessionRepository sessionRepository;

	@Autowired
	private TestApplicationService testService;

	@Autowired
	private AuthenticationProvider authProvider;

	/**
	 * Retrieves the currently logged in user.
	 *
	 * @return A transfer containing the username and the roles.
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("me")
	public UserTransfer getUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && !authentication.isAuthenticated()) {
			authentication = this.authProvider.authenticate(authentication);
		}
		String username = authentication != null ? authentication.getName() : null;
		Collection<? extends GrantedAuthority> authorities = authentication != null ? authentication.getAuthorities() : null;
		return new UserTransfer(username, authorities);
	}

	/**
	 * Authenticates a user and creates an authentication token.
	 *
	 * @param username The name of the user.
	 * @param password The password of the user.
	 * @return A transfer containing the authentication token.
	 */
	@Path("authenticate")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public TokenTransfer authenticate(@FormParam("username") String username, @FormParam("password") String password) {
		UserDetails user = this.authenticationService.authenticate(username, password);
		Authentication auth = new UserDetailsAuthentication(user);
		SecurityContextHolder.getContext().setAuthentication(auth);

		/*
		 * Reload user as password of authentication principal will be null after authorization and password is needed for token generation
		 */
		// UserDetails userDetails = this.userService.loadUserByUsername(username);

		String token = sessionRepository.createSessionId(username);
		return new TokenTransfer(token);
	}

	private Map<String, Boolean> createRoleMap(UserDetails userDetails) {
		Map<String, Boolean> roles = new HashMap<>();
		for (GrantedAuthority authority : userDetails.getAuthorities()) {
			roles.put(authority.getAuthority(), Boolean.TRUE);
		}

		return roles;
	}

	private boolean isAdmin() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Object principal = authentication.getPrincipal();
		if (principal instanceof String && ((String) principal).equals("anonymousUser")) {
			return false;
		}
		UserDetails userDetails = (UserDetails) principal;

		for (GrantedAuthority authority : userDetails.getAuthorities()) {
			if (authority.toString().equals("admin")) {
				return true;
			}
		}

		return false;
	}

	@Path("service1/{param}")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String service1(@PathParam("param") String param) {
		System.out.println(SecurityContextHolder.getContext().getAuthentication());
		return this.testService.service1(param);
	}

	@Path("service2/{param}")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String service2(@PathParam("param") String param) {
		return this.testService.service2(param);
	}
}