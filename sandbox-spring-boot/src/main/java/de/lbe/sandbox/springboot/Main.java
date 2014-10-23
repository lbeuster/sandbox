package de.lbe.sandbox.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author lbeuster
 */
@EnableAutoConfiguration
@ComponentScan
@Configuration
public class Main {

	public static ConfigurableApplicationContext main() throws Exception {
		SpringApplication app = new SpringApplication(Main.class);
		app.setShowBanner(false);
		app.setWebEnvironment(true);
		return app.run(new String[] {});
	}
}