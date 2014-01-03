package de.lbe.sandbox.deltaspike.security;

import javax.enterprise.inject.Typed;

import org.apache.deltaspike.security.api.authorization.SecurityViolation;

/**
 * @author lbeuster
 */
@Typed
public class DefaultSecurityViolation implements SecurityViolation {

	private static final long serialVersionUID = 1L;

	private final String reason;

	public DefaultSecurityViolation(String reason) {
		this.reason = reason;
	}

	@Override
	public String getReason() {
		return this.reason;
	}
}