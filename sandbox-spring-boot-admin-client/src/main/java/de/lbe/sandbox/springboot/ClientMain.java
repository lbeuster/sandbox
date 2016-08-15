package de.lbe.sandbox.springboot;

import java.util.Properties;

import org.springframework.boot.Banner.Mode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import de.asideas.ipool.commons.lib.spring.boot.web.client.RestTemplateBuilder;
import de.asideas.ipool.commons.lib.spring.web.client.PreemptiveBasicAuthClientHttpRequestInterceptor;
import de.codecentric.boot.admin.config.AdminClientProperties;
import de.codecentric.boot.admin.config.AdminProperties;
import de.codecentric.boot.admin.services.ApplicationRegistrator;

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

	/**
	 * Task that registers the application at the spring-boot-admin application.
	 */
	@Bean
	public ApplicationRegistrator registrator(AdminProperties properties, AdminClientProperties clientProperties) {
		RestTemplateBuilder builder = new RestTemplateBuilder();
		builder.messageConverters(new MappingJackson2HttpMessageConverter());
		if (properties.getUsername() != null) {
			builder.interceptors(new PreemptiveBasicAuthClientHttpRequestInterceptor(properties.getUsername(), properties.getPassword()));
		}
		return new ApplicationRegistrator(builder.build(), properties, clientProperties);
	}
}