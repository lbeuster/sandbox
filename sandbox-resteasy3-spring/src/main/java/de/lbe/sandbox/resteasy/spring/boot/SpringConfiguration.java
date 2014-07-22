package de.lbe.sandbox.resteasy.spring.boot;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import de.asideas.lib.commons.spring.validation.InjectionAwareValidationConfiguration;

@Configuration
@ComponentScan("de.lbe")
@Import(InjectionAwareValidationConfiguration.class)
public class SpringConfiguration {
}