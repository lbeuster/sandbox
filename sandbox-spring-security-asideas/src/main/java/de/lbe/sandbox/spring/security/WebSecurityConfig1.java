package de.lbe.sandbox.spring.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;

import de.lbe.sandbox.spring.security.security.AuthenticationTokenProcessingFilter;
import de.lbe.sandbox.spring.security.security.XAuthTokenAuthenticationProvider;

@Configuration
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

		//
		// <security:http realm="Protected API" use-expressions="true" auto-config="false" entry-point-ref="unauthorizedEntryPoint"
		// authentication-manager-ref="authenticationManager">
		// <security:custom-filter ref="authenticationTokenProcessingFilter" position="FORM_LOGIN_FILTER" />
		// <!-- <security:intercept-url pattern="/rest/user/authenticate" access="permitAll" /> -->
		// </security:http>

		// @Override
		// protected void configure(HttpSecurity http) throws Exception {
		// http
		// .authenticationProvider(authenticationProvider)
		// .addFilterBefore(authenticationFilter, AnonymousAuthenticationFilter.class)
		// .authorizeUrls().anyRequest().anonymous()
		// .and()
		// .authorizeUrls()
		// .antMatchers("/authorize","confirm_access","/custom/authorize")
		// .hasRole("USER")
		// .and()
		// .exceptionHandling().accessDeniedPage("/errors/access-denied.html");
		// }
		// }
		//
		// @Override
		// protected void configure(HttpSecurity http) throws Exception {
		// http
		// .userDetailsService(clientDetailsUserService)
		// .anonymous().disable()
		// .authorizeUrls()
		// .antMatchers("/token")
		// .fullyAuthenticated()
		// .and()
		// .httpBasic()
		// .authenticationEntryPoint(oauthAuthenticationEntryPoint)
		// .and()
		// .addFilterBefore(clientCredentialsTokenEndpointFilter, BasicAuthenticationFilter.class)
		// .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.stateless)
		// .and()
		// .exceptionHandling().accessDeniedHandler(oauthAccessDeniedHandler);
		// }
		//
		// }

		//
		// <security:http realm="Protected API" use-expressions="true" auto-config="false"
		// entry-point-ref="unauthorizedEntryPoint"
		// authentication-manager-ref="authenticationManager">
		// <security:custom-filter ref="authenticationTokenProcessingFilter" position="FORM_LOGIN_FILTER" />
		// <security:intercept-url pattern="/rest/user/authenticate" access="permitAll" />
		// <security:intercept-url method="GET" pattern="/rest/news/**" access="hasRole('user')" />
		// <security:intercept-url method="PUT" pattern="/rest/news/**" access="hasRole('admin')" />
		// <security:intercept-url method="POST" pattern="/rest/news/**" access="hasRole('admin')" />
		// <security:intercept-url method="DELETE" pattern="/rest/news/**" access="hasRole('admin')" />
		// </security:http>
		//
	}
}