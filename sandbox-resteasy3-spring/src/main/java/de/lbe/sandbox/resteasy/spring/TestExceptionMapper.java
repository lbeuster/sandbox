package de.lbe.sandbox.resteasy.spring;

import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.springframework.stereotype.Component;

import de.asideas.lib.commons.lang.ExceptionUtils;

/**
 * @author lars.beuster
 */
@Provider
@Component
public class TestExceptionMapper implements ExceptionMapper<Throwable> {

	@Override
	public Response toResponse(Throwable exception) {
		String entity = ExceptionUtils.toString(exception);
		if (exception instanceof ConstraintViolationException) {
			return Response.status(Response.Status.BAD_REQUEST).entity(entity).build();
		}
		return Response.serverError().entity(entity).build();
	}

}
