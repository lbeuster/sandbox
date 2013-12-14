package de.lbe.sandbox.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;

import de.asideas.lib.commons.shrinkwrap.ShrinkWrapUtils;

/**
 * @author lars.beuster
 */
public class ValidationTest extends AbstractValidationTest {

	/**
	 * 
	 */
	@Test
	public void testInjectionInValidator() {
		TestBean bean = new TestBean();
		assertIsValid(bean);
	}

	/**
	 * 
	 */
	public static class TestBean {

		@Injected
		String injected;
	}

	/**
	 * 
	 */
	@Constraint(validatedBy = { InjectedValidator.class })
	@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE })
	@Retention(RetentionPolicy.RUNTIME)
	public static @interface Injected {
		String message() default "Not Injected";

		Class<?>[] groups() default {};

		Class<? extends Payload>[] payload() default {};
	}

	/**
	 * 
	 */
	public static class InjectedValidator implements ConstraintValidator<Injected, String> {

		@Inject
		private BeanManager beanManager;

		@Override
		public void initialize(Injected constraintAnnotation) {
		}

		@Override
		public boolean isValid(String value, ConstraintValidatorContext context) {
			return this.beanManager != null;
		}
	}
}
