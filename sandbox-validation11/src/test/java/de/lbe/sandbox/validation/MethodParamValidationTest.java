package de.lbe.sandbox.validation;

import java.lang.reflect.Method;
import java.util.Set;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.constraints.NotNull;

import static org.hamcrest.Matchers.hasSize;

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
	@Test
	public void testValidateMethodParams() throws Exception {
		Method method = TestService.class.getMethod("service", String.class);
		Set<ConstraintViolation<TestService>> violations = this.validator.forExecutables().validateParameters(this.service, method, new Object[] { null });
		assertThat(violations, hasSize(1));

		violations = this.validator.forExecutables().validateParameters(this.service, method, new Object[] { "" });
		assertThat(violations, hasSize(0));
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
