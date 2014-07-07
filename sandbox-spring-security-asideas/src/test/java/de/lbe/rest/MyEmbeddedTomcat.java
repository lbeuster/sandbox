package de.lbe.rest;

import de.asideas.lib.commons.tomcat.embedded.EmbeddedTomcat;

public class MyEmbeddedTomcat extends EmbeddedTomcat {

	public MyEmbeddedTomcat() {
		setWebappDir("src/main/webapp");
	}

}