package de.lbe.sandbox.deltaspike.security;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.inject.Inject;

import org.apache.deltaspike.security.api.authorization.Secures;
import org.apache.deltaspike.security.api.authorization.SecurityBindingType;
import org.apache.deltaspike.security.api.authorization.SecurityParameterBinding;
import org.junit.Test;

import de.lbe.sandbox.deltaspike.security.AccessDeniedException;

/**
 * @author lars.beuster
 */
public class ParameterTest extends AbstractSecurityTest {

	@Inject
	private ServiceWithAnnotatedParam service;

	/**
	 * 
	 */
	@Test
	public void testAccessParam() {
		String param = "PARAM";
		try {
			service.service(param);
			fail("expecting exception");
		} catch (AccessDeniedException ex) {
			assertEquals(param, ex.getViolations().iterator().next().getReason());
		}
	}

	/**
	 * 
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ ElementType.PARAMETER })
	@Documented
	@SecurityParameterBinding
	public @interface MyParam {
		// no impl
	}

	/**
	 * 
	 */
	@Retention(value = RetentionPolicy.RUNTIME)
	@Target({ ElementType.TYPE, ElementType.METHOD })
	@Documented
	@SecurityBindingType
	public @interface MyBinding {
		// no impl
	}

	/**
	 *
	 */
	static class MyAuthorizer {

		@MyBinding
		@Secures
		public boolean doSecuredCheck(@MyParam String param) {
			throw new AccessDeniedException(param);
		}
	}

	/**
	 * 
	 */
	static class ServiceWithAnnotatedParam {

		@MyBinding
		String service(@MyParam String param) {
			return param;
		}
	}
}
