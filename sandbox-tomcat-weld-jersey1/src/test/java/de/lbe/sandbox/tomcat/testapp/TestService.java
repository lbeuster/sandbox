package de.lbe.sandbox.tomcat.testapp;

import javax.validation.Valid;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

/**
 * @author lars.beuster
 */
public class TestService {

	private UriInfo uriInfo;
	
	public TestBean service(@Valid TestBean testBean) {
		return testBean;
	}
	
	@Context
	public void setUriInfo(UriInfo uriInfo) {
		this.uriInfo = uriInfo;
	}
	
	public UriInfo getUriInfo() {
		return this.uriInfo;
	}
}
