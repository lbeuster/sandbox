package de.lbe.sandbox.validation;

import java.util.Set;

import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Before;

import de.asideas.lib.commons.arquillian.AbstractJUnit4ArquillianTest;
import de.asideas.lib.commons.cdi.validation.CdiAwareConstraintValidatorFactory;
import de.asideas.lib.commons.shrinkwrap.ShrinkWrapUtils;

/**
 * @author lars.beuster
 */
public abstract class AbstractValidationTest extends AbstractJUnit4ArquillianTest {

	@Inject
	BeanManager beanManager;

	protected Validator validator;

	/**
	 * 
	 */
	@Deployment
	public static JavaArchive deployment() {
		JavaArchive archive = ShrinkWrapUtils.prepareCdiJar();
		return archive;
	}

	/**
     *
     */
	@Before
	public void initValidator() {
		this.validator =
			Validation.byDefaultProvider().configure().constraintValidatorFactory(new CdiAwareConstraintValidatorFactory(beanManager)).buildValidatorFactory().getValidator();
	}

	/**
     *
     */
	protected <T> void assertIsValid(T object) {
		Set<ConstraintViolation<T>> violations = this.validator.validate(object);
		assertThat(violations, hasSize(0));
	}

	/**
     *
     */
	protected <T> Set<ConstraintViolation<T>> assertIsInvalid(T object) {
		Set<ConstraintViolation<T>> violations = this.validator.validate(object);
		assertThat(violations, not(hasSize(0)));
		return violations;
	}
}
