package de.lbe.sandbox.springboot.jersey2;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

import de.asideas.ipool.commons.lib.spring.annotation.Prototype;

@ApplicationPath("/rest")
@Component
@Prototype
public class HelloApplication extends ResourceConfig {

	public HelloApplication() {
		packages(getClass().getPackage().getName());
	}
}