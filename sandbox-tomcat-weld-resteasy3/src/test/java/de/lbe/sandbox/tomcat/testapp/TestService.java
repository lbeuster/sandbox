package de.lbe.sandbox.tomcat.testapp;

import javax.validation.Valid;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

/**
 * @author lars.beuster
 */
public class TestService {

	@Context
	private UriInfo uriInfo;

	public TestBean service(@Valid TestBean testBean) {
		return testBean;
	}

	public UriInfo uriInfo() {
		return this.uriInfo;
	}
}
