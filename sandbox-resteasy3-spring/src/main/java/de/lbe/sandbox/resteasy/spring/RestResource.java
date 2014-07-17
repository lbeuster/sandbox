package de.lbe.sandbox.resteasy.spring;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

/**
 * @author lars.beuster
 */
@Path("/test")
@Component
@Validated
public class RestResource {

	@Context
	private UriInfo uriInfo;

	@Context
	private HttpServletRequest httpRequest;

	@Autowired
	private TestService service;

	public RestResource() {
		System.out.println(getClass().getName() + "#<init>");
	}

	@GET
	@Produces("text/plain")
	public String test() {
		return this.service.service("hallo");
	}

	@GET
	@Produces("text/plain")
	@Path("methodValidation")
	public String testMethodValidation(@NotNull @QueryParam("param") String param) {
		return param;
	}
}
