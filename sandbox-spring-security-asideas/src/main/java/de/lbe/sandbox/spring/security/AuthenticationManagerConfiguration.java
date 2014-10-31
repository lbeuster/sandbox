package de.lbe.sandbox.spring.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;

/**
 * @author lbeuster
 */
@Configuration
public class AuthenticationManagerConfiguration {

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationManagerBuilder auth) {
		return auth.getOrBuild();
	}
}