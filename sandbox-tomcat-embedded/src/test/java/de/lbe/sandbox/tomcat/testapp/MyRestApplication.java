package de.lbe.sandbox.tomcat.testapp;

import org.glassfish.jersey.server.ResourceConfig;

public class MyRestApplication extends ResourceConfig  {

	public MyRestApplication() {
		packages(getClass().getPackage().getName());
	}
}