package net.dontdrinkandroot.example.angularrestspringsecurity;

import java.util.Collections;

import net.dontdrinkandroot.example.angularrestspringsecurity.security.XAuthTokenAuthenticationProvider;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;

@Configuration
public class SpringBeanProducer {

	// @Bean
	// public PasswordEncoder passwordEncoder() {
	// return new StandardPasswordEncoder("mySecret");
	// }

	// @Bean
	// public DaoAuthenticationProvider daoAuthenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
	// DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
	// provider.setPasswordEncoder(passwordEncoder);
	// provider.setUserDetailsService(userDetailsService);
	// return provider;
	// }

	@Bean
	public AuthenticationManager authenticationManager(XAuthTokenAuthenticationProvider authProvider) {
		ProviderManager manager = new ProviderManager(Collections.<AuthenticationProvider> singletonList(authProvider));
		return manager;
	}
}