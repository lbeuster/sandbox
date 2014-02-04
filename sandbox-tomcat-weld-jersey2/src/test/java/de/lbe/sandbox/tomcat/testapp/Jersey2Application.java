package de.lbe.sandbox.tomcat.testapp;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;

public class Jersey2Application extends ResourceConfig {

	public Jersey2Application() {
		packages(Jersey2Application.class.getPackage().getName());
		register(MultiPartFeature.class);
	}
}