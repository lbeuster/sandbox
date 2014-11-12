package de.lbe.sandbox.springboot.resteasy;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.springframework.stereotype.Component;

/**
 * @author lars.beuster
 */
@Path("/hello")
@Component
public class HelloResource {

	@GET
	@Produces("text/plain")
	public String test() {
		return "HALLO";
	}
}
