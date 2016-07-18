package de.lbe.sandbox.spring.security;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author lbeuster
 */
@Configuration
@Order(SecurityProperties.BASIC_AUTH_ORDER - 1)
@EnableWebSecurity
public class WebSecurityConfigurer1 extends WebSecurityConfigurerAdapter {

	/**
	*
	*/
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		System.out.println("Conf1: " + http);

		// @formatter:off
		http.antMatcher("/path1/**").authorizeRequests()
			.antMatchers("/path1/public").permitAll()
			.antMatchers("/path1/protected").hasAuthority("role1")
		.and().httpBasic();
		// @formatter:on
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(new AuthenticationProvider1()).eraseCredentials(false);
	}
}
