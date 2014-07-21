package de.lbe.sandbox.resteasy.spring;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import de.asideas.lib.commons.metrics.spring.Timed;

/**
 * @author lars.beuster
 */
@Component
@Validated
@Timed
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE })
public @interface RestResourcePrototype {
}
