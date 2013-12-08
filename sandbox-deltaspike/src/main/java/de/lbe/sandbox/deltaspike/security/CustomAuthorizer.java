package de.lbe.sandbox.deltaspike.security;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.spi.BeanManager;
import javax.interceptor.InvocationContext;

import org.apache.deltaspike.security.api.authorization.Secures;

@ApplicationScoped
public class CustomAuthorizer {

	@Secures
	@CustomSecurityBinding
	public boolean doSecuredCheck(InvocationContext invocationContext, BeanManager manager, LoggedInUser user) throws Exception {
		System.out.println("SECURITY");
		return user.isValid();
	}
}