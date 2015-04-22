package de.lbe.sandbox.spring.security.security;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import de.lbe.sandbox.spring.security.security.WebSecurityConfigurer.ApiWebSecurityConfigurationAdapter;

@Configuration
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
// @EnableWebSecurity
@Import(ApiWebSecurityConfigurationAdapter.class)
public class WebSecurityConfigurer {

	public static final String ANONYMOUS_USERNAME = "__anonymous__";

	// @Autowired
	// private XAuthTokenAuthenticationProvider authenticationProvider;
	//
	// @Autowired
	// private AuthenticationTokenProcessingFilter authFilter;
	//
	// @Inject
	// private SecurityProperties security;
	//
	// @Override
	// protected void configure(HttpSecurity http) throws Exception {
	//
	// // http.a
	// http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	// http.addFilterBefore(authFilter, AnonymousAuthenticationFilter.class);
	// http.csrf().disable();
	// http.anonymous().authorities(ANONYMOUS_USERNAME).principal(ANONYMOUS_USERNAME).and().antMatcher("/rest/main/**").authenticationProvider(authenticationProvider);
	//
	// // http.authorizeRequests().anyRequest().fullyAuthenticated().and().formLogin().loginPage("/login").failureUrl("/login?error").permitAll();
	// }

	//
	// @Override
	// public void configure(AuthenticationManagerBuilder auth) throws Exception {
	// auth.authenticationProvider(authenticationProvider);
	// User user = this.security.getUser();
	// auth.inMemoryAuthentication().withUser(user.getName()).password(user.getPassword()).roles(user.getRole().toArray(new String[0]));// .and().and().build();
	// //
	// // auth.inMemoryAuthentication().withUser("admin").password("admin").roles("ADMIN", "USER").and().withUser("user").password("user").roles("USER");
	// }

	@Configuration
	@Order(1)
	public static class ApiWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.antMatcher("/api/**").authorizeRequests().anyRequest().hasRole("api").and().httpBasic();
		}

		@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
			auth.inMemoryAuthentication().withUser("user").password("password").roles("api");// .and().withUser("admin").password("password").roles("USER", "ADMIN");
		}

	}

	// @Configuration
	// public static class FormLoginWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
	//
	// @Override
	// protected void configure(HttpSecurity http) throws Exception {
	// http.authorizeRequests().anyRequest().authenticated().and().formLogin();
	// }
	// }
}