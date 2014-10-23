package de.lbe.sandbox.springboot.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

public class AuthenticationTokenProcessingFilter extends GenericFilterBean {

	private final SessionRepository sessionRepository;

	public AuthenticationTokenProcessingFilter(SessionRepository sessionRepository) {
		this.sessionRepository = sessionRepository;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = this.getAsHttpRequest(request);

		String authToken = this.extractAuthTokenFromRequest(httpRequest);
		if (authToken != null) {

			// }
			// String userName = sessionRepository.getUserBySessionId(authToken);
			// System.out.println("auth-filter: " + authToken + ":" + userName);
			//
			// if (userName != null) {
			//
			// UserDetails userDetails = this.userService.loadUserByUsername(userName);
			// UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
			// authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
			XAuthTokenAuthentication auth = new XAuthTokenAuthentication(authToken);
			auth.setAuthenticated(false);
			SecurityContextHolder.getContext().setAuthentication(auth);
		}

		chain.doFilter(request, response);
	}

	private HttpServletRequest getAsHttpRequest(ServletRequest request) {
		if (!(request instanceof HttpServletRequest)) {
			throw new RuntimeException("Expecting an HTTP request");
		}

		return (HttpServletRequest) request;
	}

	private String extractAuthTokenFromRequest(HttpServletRequest httpRequest) {
		/* Get token from header */
		String authToken = httpRequest.getHeader("X-Auth-Token");

		/* If token not found get it from request parameter */
		if (authToken == null) {
			authToken = httpRequest.getParameter("token");
		}

		return authToken;
	}
}