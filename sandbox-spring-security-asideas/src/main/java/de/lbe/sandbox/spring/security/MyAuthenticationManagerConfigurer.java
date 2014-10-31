package de.lbe.sandbox.spring.security;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;

import de.lbe.sandbox.spring.security.security.XAuthTokenAuthenticationProvider;

@Configuration
public class MyAuthenticationManagerConfigurer extends GlobalAuthenticationConfigurerAdapter {

	@Autowired
	private XAuthTokenAuthenticationProvider authenticationProvider;

	@Inject
	private SecurityProperties security;

	@Override
	public void init(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider);
		User user = this.security.getUser();
		auth.inMemoryAuthentication().withUser(user.getName()).password(user.getPassword()).roles(user.getRole().toArray(new String[0]));// .and().and().build();
	}
}