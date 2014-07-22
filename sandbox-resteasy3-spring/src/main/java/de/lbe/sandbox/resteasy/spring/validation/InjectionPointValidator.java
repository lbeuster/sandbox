package de.lbe.sandbox.resteasy.spring.validation;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;

import de.asideas.lib.commons.validation.AbstractConstraintValidator;
import de.lbe.sandbox.resteasy.spring.TestService;

/**
 * @author lbeuster
 */
public class InjectionPointValidator extends AbstractConstraintValidator<ValidInjectionPoint, String> {

	@Autowired
	private TestService testService;

	public InjectionPointValidator() {

	}

	@Override
	public boolean isValidImpl(String param) {
		Objects.requireNonNull(this.testService, "injectedTestService");
		return true;
	}
}
