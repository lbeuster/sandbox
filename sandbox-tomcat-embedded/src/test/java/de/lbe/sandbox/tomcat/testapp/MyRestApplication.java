package de.lbe.sandbox.tomcat.testapp;

import com.sun.jersey.api.core.PackagesResourceConfig;

public class MyRestApplication extends PackagesResourceConfig {

	public MyRestApplication() {
		super(MyRestApplication.class.getPackage().getName());
	}
}