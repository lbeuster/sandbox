package de.lbe.sandbox.springboot;

import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import de.asideas.lib.commons.spring.boot.tomcat.EmbeddedTomcatFactory;
import de.asideas.lib.commons.spring.boot.tomcat.EmbeddedTomcatMDCInitializer;
import de.asideas.lib.commons.tomcat.embedded.DefaultAccessLogValve;

@Configuration
public class TomcatConfiguration {

	@Bean
	public EmbeddedServletContainerFactory servletContainer() {
		EmbeddedTomcatFactory factory = new EmbeddedTomcatFactory();
		factory.setPort(9000);
		// factory.setSessionTimeout(1, TimeUnit.MINUTES);
		factory.addContextValves(new DefaultAccessLogValve().logRequestEntry(true).mdcInitializer(new EmbeddedTomcatMDCInitializer(factory)));
		return factory;
	}
}
