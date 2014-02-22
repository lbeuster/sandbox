package de.lbe.tomee.webapp.rest;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

/**
 * <p>
 * A Jersey provider which adds validation to the basic Jackson Json provider. Any request entity method parameters annotated with {@code @Valid} are validated, and an informative
 * {@code 422 Unprocessable Entity} response is returned should the entity be invalid. <br/>
 * </p>
 * <p>
 * Thanks to Yammer's Dropwizard for the original idea.
 * </p>
 * 
 * @author lbeuster
 */
@Provider
@Consumes({ MediaType.APPLICATION_JSON, "text/json" })
@Produces({ MediaType.APPLICATION_JSON, "text/json" })
@ApplicationScoped
public class ValidatingJacksonJsonProvider implements MessageBodyReader<Object> {

	/**
	 * 
  	 */
	private MessageBodyReader<Object> delegate;

	/**
	 * 
	 */
	@Inject
	private Validator validator;

	/**
	 * 
	 */
	public ValidatingJacksonJsonProvider() {
		delegate = new JacksonJsonProvider();
	}

	/**
	 * 
	 */
	@Override
	public Object readFrom(Class<Object> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
		throws IOException {

		// delegate the parsing
		Object value = delegate.readFrom(type, genericType, annotations, mediaType, httpHeaders, entityStream);

		// validate
		if (hasValidAnnotation(annotations)) {
			if (value instanceof JsonHello) {
				((JsonHello) value).setValidated(true);
			}
			Set<ConstraintViolation<Object>> violations = this.validator.validate(value);
			if (!violations.isEmpty()) {
				StringBuilder msg = new StringBuilder("The request entity had the following errors:\n");
				for (ConstraintViolation<Object> violation : violations) {
					msg.append(" * ").append(violation).append('\n');
				}
				throw new ValidationException(msg.toString());
			}
		}
		return value;
	}

	@Override
	public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return delegate.isReadable(type, genericType, annotations, mediaType);
	}

	/**
	 * 
	 */
	private static boolean hasValidAnnotation(Annotation[] annotations) {
		for (Annotation annotation : annotations) {
			if (Valid.class == annotation.annotationType()) {
				return true;
			}
		}
		return false;
	}
}
