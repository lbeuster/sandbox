package de.lbe.sandbox.deltaspike.security;

import org.apache.deltaspike.security.api.authorization.Secures;

/**
 * @author lbeuster
 */
public class DenyAllAuthorizer {

	@Secures
	@MyDenyAll
	public boolean denyAll() {
		throw new AccessDeniedException("denyAll");
	}
}