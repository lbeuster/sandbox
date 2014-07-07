package de.lbe.sandbox.deltaspike.security;

import java.util.Set;

import org.apache.deltaspike.security.api.authorization.AccessDecisionVoter;
import org.apache.deltaspike.security.api.authorization.AccessDecisionVoterContext;
import org.apache.deltaspike.security.api.authorization.SecurityViolation;

/**
 * @author lbeuster
 */
public class HasPermissionAccessDecisionVoter implements AccessDecisionVoter {

	private static final long serialVersionUID = 1L;

	@Override
	public Set<SecurityViolation> checkPermission(AccessDecisionVoterContext context) {
		HasPermission annotation = context.getMetaDataFor(HasPermission.class.getName(), HasPermission.class);
		String permission = annotation.value();
		System.out.println(permission);
		return null;
	}
}