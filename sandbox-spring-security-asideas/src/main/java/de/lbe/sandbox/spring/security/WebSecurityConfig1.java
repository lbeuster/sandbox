package de.lbe.sandbox.spring.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;

import de.lbe.sandbox.spring.security.security.AuthenticationTokenProcessingFilter;
import de.lbe.sandbox.spring.security.security.XAuthTokenAuthenticationProvider;

//@Configuration
public class WebSecurityConfig1 extends WebSecurityConfigurerAdapter {

	public static final String ANONYMOUS_USERNAME = "__anonymous__";

	@Autowired
	private XAuthTokenAuthenticationProvider authenticationProvider;

	@Autowired
	private AuthenticationTokenProcessingFilter authFilter;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// @Autowired
		// public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		// auth.userDetailsService(this.userDetailsService);
		auth.authenticationProvider(authenticationProvider);

		// System.out.println(this.userDetailsService);
		// auth.inMemoryAuthentication().withUser("user") // #1
		// .password("password").roles("USER").and().withUser("admin") // #2
		// .password("password").roles("ADMIN", "USER");
	}

	// @Override
	// public void configure(WebSecurity web) throws Exception {
	// web.ignoring().antMatchers("/resources/**"); // #3
	// }

	// @Override
	// protected void configure(HttpSecurity http) throws Exception {
	// http.authorizeRequests().antMatchers("/signup", "/about").permitAll() // #4
	// .antMatchers("/admin/**").hasRole("ADMIN") // #6
	// .anyRequest().authenticated() // 7
	// .and().formLogin() // #8
	// .loginPage("/login") // #9
	// .permitAll(); // #5
	// }

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// http.a
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.addFilterBefore(authFilter, AnonymousAuthenticationFilter.class);
		http.csrf().disable();
		http.anonymous().authorities(ANONYMOUS_USERNAME).principal(ANONYMOUS_USERNAME);
		http.antMatcher("/rest/main/**");
	}
}