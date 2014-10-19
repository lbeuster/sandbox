package de.lbe.sandbox.springboot;

import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lbeuster
 */
@Configuration
public class ServletConfiguration {

	@Bean
	public ServletRegistrationBean testServlet1() {
		return new ServletRegistrationBean(new TestServlet(), "/admin/*");
	}

	@Bean
	public ServletRegistrationBean testServlet2() {
		return new ServletRegistrationBean(new TestServlet2(), "/admin2/*");
	}
}