package de.lbe.sandbox.springboot.jersey1;

import java.util.Objects;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author lbeuster
 */
@Provider
@Component
public class DefaultExceptionMapper implements ExceptionMapper<Exception> {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultExceptionMapper.class);

	public static final String ENTITY = "ExceptionMapper";

	@Autowired
	private HelloService helloService;

	@Override
	public Response toResponse(Exception exception) {
		LOGGER.error("DefaultExceptionMapper", exception);
		Objects.requireNonNull(this.helloService, "helloService");
		return Response.serverError().entity(ENTITY).build();
	}
}
