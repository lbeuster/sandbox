package de.lbe.sandbox.spring.security.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Component;

@Component
@Path("/basic")
public class BasicAuthResource {

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("")
	public String get() {
		return "OK";
	}
}