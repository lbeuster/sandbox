package de.lbe.sandbox.springboot.jersey1;

import com.sun.jersey.api.core.PackagesResourceConfig;

//@ApplicationPath("/")
public class HelloApplication extends PackagesResourceConfig {

	public HelloApplication() {
		super(HelloApplication.class.getPackage().getName());
	}
}