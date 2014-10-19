package de.lbe.sandbox.springboot;

import java.util.concurrent.TimeUnit;

import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import de.asideas.lib.commons.spring.boot.tomcat.EmbeddedTomcatFactory;

@Configuration
public class TomcatConfiguration {

	@Bean
	public EmbeddedServletContainerFactory servletContainer() {
		EmbeddedTomcatFactory factory = new EmbeddedTomcatFactory();
		factory.setPort(9000);
		factory.setSessionTimeout(1, TimeUnit.MINUTES);
		return factory;
	}
}
