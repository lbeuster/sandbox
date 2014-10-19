package de.lbe.sandbox.springboot.old;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import de.asideas.lib.commons.spring.beans.ExtendedAnnotationBeanNameGenerator;
import de.asideas.lib.commons.spring.beans.ExtendedListableBeanFactory;

/**
 * @author lbeuster
 */
public class SpringApplicationContext extends AnnotationConfigWebApplicationContext {

	/**
	 * 
	 */
	public SpringApplicationContext() {
		// we want long bean names
		setBeanNameGenerator(new ExtendedAnnotationBeanNameGenerator());
	}

	/**
	 * 
	 */
	@Override
	protected DefaultListableBeanFactory createBeanFactory() {
		ExtendedListableBeanFactory beanFactory = new ExtendedListableBeanFactory(getInternalParentBeanFactory());
		beanFactory.setAllowBeanDefinitionOverriding(false);
		beanFactory.setDisableAutowiringByName(true);
		return beanFactory;
	}
}
