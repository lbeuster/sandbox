package de.lbe.sandbox.tomcat.testapp;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * @author lars.beuster
 */
@Constraint(validatedBy = { TestBeanValidator.class })
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidTestBean {

	String message() default "invalid name";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
