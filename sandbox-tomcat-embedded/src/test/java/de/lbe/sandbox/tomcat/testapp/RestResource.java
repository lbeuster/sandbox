package de.lbe.sandbox.tomcat.testapp;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * @author lars.beuster
 */
@Path("/rest")
public class RestResource {

	@GET
	@Produces("text/plain")
	public String service() {
		return "HALLO";
	}
}
