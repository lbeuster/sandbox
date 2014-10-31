package de.lbe.sandbox.spring.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;

import de.asideas.lib.commons.spring.boot.security.SpringBootAuthenticationManagerConfigurer;
import de.lbe.sandbox.spring.security.security.XAuthTokenAuthenticationProvider;

@Configuration
@Import(SpringBootAuthenticationManagerConfigurer.class)
public class MyAuthenticationManagerConfigurer extends GlobalAuthenticationConfigurerAdapter {

	// @Autowired
	// private List<SecurityPrequisite> dependencies;

	@Autowired
	private XAuthTokenAuthenticationProvider authenticationProvider;

	@Override
	public void init(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider);
		// User user = this.security.getUser();
		// auth.inMemoryAuthentication().withUser(user.getName()).password(user.getPassword()).roles(user.getRole().toArray(new String[0]));// .and().and().build();
	}
}