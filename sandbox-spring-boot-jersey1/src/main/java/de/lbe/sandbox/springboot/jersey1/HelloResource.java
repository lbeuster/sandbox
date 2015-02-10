package de.lbe.sandbox.springboot.jersey1;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.springframework.stereotype.Component;

/**
 * @author lars.beuster
 */
@Path("/api/hello")
@Component
public class HelloResource {

	@GET
	@Produces("text/plain")
	public String test() {
		return "HALLO";
	}

	@GET
	@Produces("text/plain")
	@Path("/exception")
	public String exceptionMapper() {
		throw new IllegalArgumentException("EXCEPTION");
	}
}
