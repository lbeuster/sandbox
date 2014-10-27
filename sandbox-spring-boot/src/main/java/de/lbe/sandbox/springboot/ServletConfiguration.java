package de.lbe.sandbox.springboot;

import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lbeuster
 */
@Configuration
public class ServletConfiguration {

	// @Bean
	// public ServletRegistrationBean testServlet1() {
	// // same URL is in web-fragment.xml
	// return new ServletRegistrationBean(new TestServlet(), "/protected/*");
	// }

	@Bean
	public ServletRegistrationBean testServlet2() {
		return new ServletRegistrationBean(new TestServlet2(), "/test/*");
	}
}