package de.lbe.sandbox.resteasy.spring;

import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

/**
 * @author lbeuster
 */
public class MySpringApplicationContext extends AnnotationConfigWebApplicationContext {

	/**
	 * 
	 */
	public MySpringApplicationContext() {
		register(MethodValidationPostProcessor.class);
		scan("de.lbe");
	}
}
