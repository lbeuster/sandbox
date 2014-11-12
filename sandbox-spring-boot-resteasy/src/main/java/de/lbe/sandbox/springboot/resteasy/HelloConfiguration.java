package de.lbe.sandbox.springboot.resteasy;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import de.asideas.lib.commons.resteasy.spring.boot.ResteasyBootstrapConfiguration;

/**
 * @author lars.beuster
 */
@Configuration
@Import({ ResteasyBootstrapConfiguration.class })
@Component
@EnableAutoConfiguration
@ComponentScan
// @EnableWebSecurity
public class HelloConfiguration {

	@GET
	@Produces("text/plain")
	public String test() {
		return "HALLO";
	}
}
