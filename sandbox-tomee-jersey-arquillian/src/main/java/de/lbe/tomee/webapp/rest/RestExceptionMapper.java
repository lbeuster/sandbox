package de.lbe.tomee.webapp.rest;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
@ApplicationScoped
public class RestExceptionMapper implements ExceptionMapper<Exception> {

	@Inject
	private SomeService someService;

	@Override
	public Response toResponse(Exception throwable) {
		String message = toErrorEntity(throwable.getMessage(), this.someService != null);
		return Response.status(444).entity(message).build();
	}

	public static String toErrorEntity(String message, boolean injectionAvailable) {
		return message + "-" + (injectionAvailable ? "YES" : "NO");
	}
}
