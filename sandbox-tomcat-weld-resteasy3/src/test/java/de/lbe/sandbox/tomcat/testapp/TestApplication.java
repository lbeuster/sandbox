package de.lbe.sandbox.tomcat.testapp;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/lars")
public class TestApplication extends Application {

	public TestApplication() {
	}
}