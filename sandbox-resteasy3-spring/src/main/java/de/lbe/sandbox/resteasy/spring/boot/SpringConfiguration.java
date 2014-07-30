package de.lbe.sandbox.resteasy.spring.boot;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import de.asideas.lib.commons.metrics.spring.config.SpringMetricsProducer;
import de.asideas.lib.commons.spring.validation.InjectionAwareValidationConfiguration;

@Configuration
@ComponentScan("de.lbe")
@Import({ InjectionAwareValidationConfiguration.class, SpringMetricsProducer.class })
public class SpringConfiguration {
}