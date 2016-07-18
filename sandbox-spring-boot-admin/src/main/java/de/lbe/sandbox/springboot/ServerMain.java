package de.lbe.sandbox.springboot;

import java.util.Properties;

import org.springframework.boot.Banner.Mode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import de.codecentric.boot.admin.config.EnableAdminServer;

/**
 * @author lbeuster
 */
@EnableAutoConfiguration
@ComponentScan
@Configuration
@EnableAdminServer
public class ServerMain {

	public static ConfigurableApplicationContext main() {
		Properties properties = new Properties();
		properties.setProperty("server.port", "8081");
		SpringApplication app = new SpringApplication(ServerMain.class);
		app.setBannerMode(Mode.OFF);
		app.setWebEnvironment(true);
		app.setDefaultProperties(properties);
		return app.run(new String[] {});
	}
}