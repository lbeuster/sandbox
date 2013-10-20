package de.lbe.sandbox.metrics.webapp;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * 
 */
@Path("")
@RequestScoped
public class HelloWorldResource {

	@GET
	@Produces({ "application/json" })
	@Path("/hello")
	public Hello getHelloWorld() {
		Hello hello = new Hello();
		hello.setMessage("HALLO");
		return hello;
	}
}
