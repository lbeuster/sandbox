package de.lbe.sandbox.spring.security.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;

@Configuration
// @Import(SpringBootAuthenticationManagerConfigurer.class)
public class MyAuthenticationManagerConfigurer extends GlobalAuthenticationConfigurerAdapter {

	// @Autowired
	// private List<SecurityPrequisite> dependencies;

	@Autowired
	private XAuthTokenAuthenticationProvider authenticationProvider;

	@Override
	public void init(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider);
		auth.inMemoryAuthentication().withUser("user").password("password").roles("USER").and().withUser("admin").password("password").roles("USER", "ADMIN");
		// User user = this.security.getUser();
		// auth.inMemoryAuthentication().withUser(user.getName()).password(user.getPassword()).roles(user.getRole().toArray(new String[0]));// .and().and().build();
	}
}