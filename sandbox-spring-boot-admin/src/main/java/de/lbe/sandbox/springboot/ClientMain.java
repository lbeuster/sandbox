package de.lbe.sandbox.springboot;

import java.util.Properties;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import de.asideas.ipool.commons.lib.spring.boot.MoreSpringBootAutoConfiguration;
import de.asideas.ipool.commons.lib.spring.boot.SpringApplication;

/**
 * @author lbeuster
 */
@EnableAutoConfiguration
@ComponentScan
@Configuration
@Import(MoreSpringBootAutoConfiguration.class)
public class ClientMain {

	public static ConfigurableApplicationContext main() {
		Properties properties = new Properties();
		properties.setProperty("server.port", "8080");
		properties.setProperty("spring.boot.admin.url", "http://localhost:8081");
		SpringApplication app = new SpringApplication(ClientMain.class);
		app.setShowBanner(false);
		app.setWebEnvironment(true);
		app.setDefaultProperties(properties);
		return app.run(new String[] {});
	}
}