package de.lbe.sandbox.springboot.jersey2;

import java.util.Objects;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author lbeuster
 */
@Provider
public class DefaultExceptionMapper implements ExceptionMapper<Exception> {

	public static final String ENTITY = "ExceptionMapper";

	@Autowired
	private HelloService helloService;

	@Override
	public Response toResponse(Exception exception) {
		Objects.requireNonNull(this.helloService, "helloService");
		return Response.serverError().entity(ENTITY).build();
	}

	// @Autowired
	// public void setHelloService(HelloService service) {
	// this.helloService = service;
	// }
}
