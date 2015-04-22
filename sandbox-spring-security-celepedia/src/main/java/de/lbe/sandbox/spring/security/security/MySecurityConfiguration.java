package de.lbe.sandbox.spring.security.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
// @formatter:off
@Import({
//	OriginalSpringBootWebSecurityConfiguration.class,
	AuthenticationTokenProcessingFilter.class,
	XAuthTokenAuthenticationProvider.class,
//	MyAuthenticationManagerConfigurer.class,
	WebSecurityConfigurer.class
})
//@formatter:on
public class MySecurityConfiguration {

	// @Bean
	// public AuthenticationManager authenticationManager(AuthenticationManagerBuilder auth) {
	// return auth.getOrBuild();
	// }
}