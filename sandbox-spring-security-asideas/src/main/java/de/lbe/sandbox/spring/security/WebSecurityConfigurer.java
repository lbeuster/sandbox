package de.lbe.sandbox.spring.security;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;

import de.lbe.sandbox.spring.security.security.AuthenticationTokenProcessingFilter;
import de.lbe.sandbox.spring.security.security.XAuthTokenAuthenticationProvider;

@Configuration
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@EnableWebSecurity
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {

	public static final String ANONYMOUS_USERNAME = "__anonymous__";

	@Autowired
	private XAuthTokenAuthenticationProvider authenticationProvider;

	@Autowired
	private AuthenticationTokenProcessingFilter authFilter;

	@Inject
	private SecurityProperties security;

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		// http.a
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.addFilterBefore(authFilter, AnonymousAuthenticationFilter.class);
		http.csrf().disable();
		http.anonymous().authorities(ANONYMOUS_USERNAME).principal(ANONYMOUS_USERNAME).and().antMatcher("/rest/main/**").authenticationProvider(authenticationProvider);

		// http.authorizeRequests().anyRequest().fullyAuthenticated().and().formLogin().loginPage("/login").failureUrl("/login?error").permitAll();
	}
	//
	// @Override
	// public void configure(AuthenticationManagerBuilder auth) throws Exception {
	// auth.authenticationProvider(authenticationProvider);
	// User user = this.security.getUser();
	// auth.inMemoryAuthentication().withUser(user.getName()).password(user.getPassword()).roles(user.getRole().toArray(new String[0]));// .and().and().build();
	// //
	// // auth.inMemoryAuthentication().withUser("admin").password("admin").roles("ADMIN", "USER").and().withUser("user").password("user").roles("USER");
	// }
}