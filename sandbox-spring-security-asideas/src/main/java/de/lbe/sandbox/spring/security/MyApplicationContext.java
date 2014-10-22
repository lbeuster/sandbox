package de.lbe.sandbox.spring.security;

import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

public class MyApplicationContext extends AnnotationConfigWebApplicationContext {

	public MyApplicationContext() {
		register(ApplicationConfiguration.class);
	}
}
