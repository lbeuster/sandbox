package de.lbe.sandbox.spring.security;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import de.asideas.ipool.commons.lib.spring.boot.SpringApplication;
import de.asideas.ipool.commons.lib.spring.boot.context.embedded.tomcat.EmbeddedTomcatFactory;
import de.lbe.sandbox.spring.security.domain.MyDomainConfiguration;
import de.lbe.sandbox.spring.security.rest.MyRestConfiguration;
import de.lbe.sandbox.spring.security.security.MySecurityConfiguration;

@Configuration
@EnableAutoConfiguration
@Import({ MyDomainConfiguration.class, MyRestConfiguration.class, MySecurityConfiguration.class })
public class Main {

	public static final void main(String[] args) throws Exception {
		SpringApplication.run(Main.class, args);
	}

	@Bean
	public EmbeddedTomcatFactory embeddedTomcatFactory() {
		return new EmbeddedTomcatFactory();
	}
}
