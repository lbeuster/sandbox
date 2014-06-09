package de.lbe.sandbox.deltaspike.security;

import org.apache.deltaspike.security.api.authorization.Secures;

/**
 * @author lbeuster
 */
public class MyDenyAllAuthorizer {

	@Secures
	@MyDenyAll
	public boolean denyAll() {
		throw new AccessDeniedException("denyAll");
	}
}