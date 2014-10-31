package de.lbe.sandbox.spring.security;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import de.asideas.lib.commons.resteasy.spring.boot.ResteasyBootstrapConfiguration;
import de.asideas.lib.commons.spring.boot.tomcat.EmbeddedTomcatFactory;

@Configuration
@ComponentScan
@EnableAutoConfiguration
@Import({ ResteasyBootstrapConfiguration.class })
public class ApplicationConfiguration {

	@Bean
	public EmbeddedTomcatFactory embeddedTomcatFactory() {
		return new EmbeddedTomcatFactory();
	}
}
