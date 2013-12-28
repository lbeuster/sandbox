package de.lbe.sandbox.tomcat.testapp;

import javax.validation.ConstraintValidatorContext;

import de.asideas.lib.commons.validation.AbstractConstraintValidator;

/**
 * @author lars.beuster
 */
public class TestBeanValidator extends AbstractConstraintValidator<ValidTestBean, TestBean> {

	@Override
	protected boolean isValidImpl(TestBean testBean, ConstraintValidatorContext context) {
		testBean.incValidationCount();
		return testBean.getName() != null;
	}
}
