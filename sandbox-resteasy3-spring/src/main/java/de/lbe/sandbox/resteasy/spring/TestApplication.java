package de.lbe.sandbox.resteasy.spring;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/api")
public class TestApplication extends Application {

	public TestApplication() {
	}
}