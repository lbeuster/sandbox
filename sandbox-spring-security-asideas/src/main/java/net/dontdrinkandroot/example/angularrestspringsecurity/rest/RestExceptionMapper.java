package net.dontdrinkandroot.example.angularrestspringsecurity.rest;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;

@Provider
public class RestExceptionMapper implements ExceptionMapper<Throwable> {

	private static final Logger LOGGER = LoggerFactory.getLogger(RestExceptionMapper.class);

	@Override
	public Response toResponse(Throwable exception) {

		LOGGER.error("Got exception in rest exception mapper", exception);
		if (exception instanceof AuthenticationException) {
			return Response.status(Response.Status.FORBIDDEN).build();
		}
		if (exception instanceof AccessDeniedException) {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
		return Response.serverError().build();
	}

}