package de.lbe.sandbox.deltaspike.security;

import org.apache.deltaspike.security.api.authorization.Secures;

/**
 * @author lbeuster
 */
public class PermitAllAuthorizer {

	@Secures
	@MyPermitAll
	public boolean permitAll() {
		return true;
	}
}