package de.lbe.sandbox.spring.security.rest;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

@ApplicationPath("/api")
@Component
public class TestApplication extends ResourceConfig {

	public TestApplication() {
		packages(getClass().getPackage().getName());
		register(RestExceptionMapper.class);
	}
}