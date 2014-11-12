package de.lbe.sandbox.springboot.resteasy;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.springframework.stereotype.Component;

import de.asideas.lib.commons.spring.annotation.Prototype;

@ApplicationPath("/rest")
@Component
@Prototype
public class HelloApplication extends Application {

	public HelloApplication() {
	}
}