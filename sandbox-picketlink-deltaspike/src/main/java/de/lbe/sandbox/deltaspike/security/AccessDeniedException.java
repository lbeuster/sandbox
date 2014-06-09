package de.lbe.sandbox.deltaspike.security;

import java.util.Collections;
import java.util.Set;

import org.apache.deltaspike.security.api.authorization.SecurityViolation;

/**
 * @author lbeuster
 */
public class AccessDeniedException extends org.apache.deltaspike.security.api.authorization.AccessDeniedException {

	private static final long serialVersionUID = 1L;

	public AccessDeniedException(Set<? extends SecurityViolation> violations) {
		super(Set.class.cast(violations));
	}

	public AccessDeniedException(String reason) {
		this(Collections.singleton(new DefaultSecurityViolation(reason)));
	}
}