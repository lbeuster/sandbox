package de.lbe.sandbox.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author lbeuster
 */
@EnableAutoConfiguration
@ComponentScan
@Configuration
public class Main {

	public static void main(String[] args) throws Exception {
		SpringApplication app = new SpringApplication(Main.class);
		app.setShowBanner(false);
		app.setWebEnvironment(true);
		app.run(args);
	}
}