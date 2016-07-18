package de.lbe.sandbox.springboot;

import java.util.Properties;

import org.springframework.boot.Banner.Mode;
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
public class ClientMain {

	public static ConfigurableApplicationContext main() {
		Properties properties = new Properties();
		properties.setProperty("server.port", "8080");
		properties.setProperty("spring.boot.admin.url", "http://localhost:8081");
		SpringApplication app = new SpringApplication(ClientMain.class);
		app.setBannerMode(Mode.OFF);
		app.setWebEnvironment(true);
		app.setDefaultProperties(properties);
		return app.run(new String[] {});
	}
}