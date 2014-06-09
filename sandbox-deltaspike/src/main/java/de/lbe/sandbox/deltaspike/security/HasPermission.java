package de.lbe.sandbox.deltaspike.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.enterprise.inject.Stereotype;

import org.apache.deltaspike.security.api.authorization.Secured;

/**
 * @author lbeuster
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
@Stereotype
@Secured(HasPermissionAccessDecisionVoter.class)
public @interface HasPermission {

	String value();
}
