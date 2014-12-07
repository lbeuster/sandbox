package de.lbe.sandbox.springboot;

import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import de.asideas.ipool.commons.lib.spring.boot.tomcat.EmbeddedTomcatFactory;
import de.asideas.ipool.commons.lib.spring.boot.tomcat.EmbeddedTomcatMDCInitializer;
import de.asideas.ipool.commons.lib.tomcat.embedded.DefaultAccessLogValve;

@Configuration
public class TomcatConfiguration {

	@Bean
	public EmbeddedServletContainerFactory servletContainer() {
		EmbeddedTomcatFactory factory = new EmbeddedTomcatFactory();
		// factory.setSessionTimeout(1, TimeUnit.MINUTES);
		factory.addContextValves(new DefaultAccessLogValve().logRequestEntry(true).mdcInitializer(new EmbeddedTomcatMDCInitializer(factory)));
		return factory;
	}
}
