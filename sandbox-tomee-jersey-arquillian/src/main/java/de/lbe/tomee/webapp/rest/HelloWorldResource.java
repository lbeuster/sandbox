package de.lbe.tomee.webapp.rest;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import de.lbe.tomee.webapp.ejb.Hello;
import de.lbe.tomee.webapp.ejb.HelloService;

/**
 * 
 */
@Path("")
@RequestScoped
public class HelloWorldResource {

	@Inject
	HelloService helloService;

	@GET
	@Path("/json")
	@Produces({ "application/json" })
	public JsonHello getHelloWorldJSON() {
		Hello message = helloService.createHello("World");
		JsonHello hello = new JsonHello();
		hello.setMessage(message.getMessage());
		hello.setResponseId(message.getId());
		return hello;
	}

	@POST
	@Path("/post")
	@Produces({ "application/json" })
	public JsonHello putHello(JsonHello hello) {
		return hello;
	}

	@GET
	@Path("/error/{message}")
	@Produces({ "application/json" })
	public JsonHello getError(@PathParam("message") String message) {
		throw new NullPointerException(message);
	}
}
