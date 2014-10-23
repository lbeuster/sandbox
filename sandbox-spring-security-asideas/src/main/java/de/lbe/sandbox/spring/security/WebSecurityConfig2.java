package de.lbe.sandbox.spring.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@Order(101)
public class WebSecurityConfig2 extends WebSecurityConfigurerAdapter {

	public static final String USERNAME = "admin";
	public static final String PASSWORD = "pwd";

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().withUser(USERNAME).password(PASSWORD).roles("_ADMIN_");
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
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.authorizeRequests().antMatchers("/rest/basic/**").authenticated();
		http.httpBasic();
		// // http.antMatcher("/rest/basic/**")
		// http.httpBasic();
	}
}