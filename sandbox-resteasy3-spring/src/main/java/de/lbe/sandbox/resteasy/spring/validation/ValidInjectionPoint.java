package de.lbe.sandbox.resteasy.spring.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * @author mdohnal
 */
@Constraint(validatedBy = { InjectionPointValidator.class })
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidInjectionPoint {

	String message() default "ValidQueryParam-message";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
