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
@EnableWebSecurity
@Order(SecurityProperties.BASIC_AUTH_ORDER - 2)
public class WebSecurityConfigurer2 extends WebSecurityConfigurerAdapter {

	/**
	*
	*/
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		System.out.println("Conf2: " + http);

		// @formatter:off
		http.antMatcher("/path2/**").authorizeRequests()
			.antMatchers("/path2/public").permitAll()
			.antMatchers("/path2/protected").hasAuthority("role2")
			.and().httpBasic();
		// @formatter:on
		// http.antMatcher("/path2/**").authorizeRequests().antMatchers("/path2/public").permitAll().antMatchers("/path2/protected").hasAuthority("role2").and().httpBasic();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(new AuthenticationProvider2());
	}
}
