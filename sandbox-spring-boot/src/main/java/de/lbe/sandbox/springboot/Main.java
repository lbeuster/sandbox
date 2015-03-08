package de.lbe.sandbox.springboot;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import de.asideas.ipool.commons.lib.spring.boot.MoreAutoConfiguration;
import de.asideas.ipool.commons.lib.spring.boot.SpringApplication;

/**
 * @author lbeuster
 */
@EnableAutoConfiguration
@ComponentScan
@Configuration
@Import(MoreAutoConfiguration.class)
public class Main {

	public static ConfigurableApplicationContext main() throws Exception {
		SpringApplication app = new SpringApplication(Main.class);
		app.setShowBanner(false);
		app.setWebEnvironment(true);
		return app.run(new String[] {});
	}
}