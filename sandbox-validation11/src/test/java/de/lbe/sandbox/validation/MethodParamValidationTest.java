package de.lbe.sandbox.validation;

import javax.inject.Inject;
import javax.validation.ConstraintViolationException;
import javax.validation.constraints.NotNull;

import org.junit.Test;

/**
 * @author lars.beuster
 */
public class MethodParamValidationTest extends AbstractValidationTest {

	@Inject
	TestService service;

	/**
	 * 
	 */
	@Test(expected = ConstraintViolationException.class)
	public void testValidateMethodParamsWithNull() throws Exception {
		service.service(null);
	}

	/**
	 * 
	 */
	@Test
	public void testValidateMethodParams() throws Exception {
		service.service("");
	}

	/**
	 * 
	 */
	public static class TestService {

		public String service(@NotNull String param) {
			return param;
		}
	}
}
