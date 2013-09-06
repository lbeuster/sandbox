package de.lbe.weld.test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.InjectionPoint;

import org.jboss.weld.literal.DefaultLiteral;

import de.asideas.lib.commons.lang.Assert;

/**
 * @author lars.beuster
 */
public abstract class AbstractConfigurableBean<T> implements Bean<T> {

	/**
	 * 
	 */
	private final Class<T> beanClass;

	/**
	 * 
	 */
	private boolean nullable = false;

	/**
	 * 
	 */
	public AbstractConfigurableBean(Class<T> beanClass) {
		Assert.isNotNull(beanClass, "beanClass");
		this.beanClass = beanClass;
	}

	@Override
	public void destroy(T instance, CreationalContext<T> creationalContext) {
		// nothing to do
	}

	@Override
	public Set<Type> getTypes() {
		return Collections.singleton((Type) this.beanClass);
	}

	@Override
	public Set<Annotation> getQualifiers() {
		return Collections.singleton((Annotation) DefaultLiteral.INSTANCE);
	}

	@Override
	public Class<? extends Annotation> getScope() {
		return ApplicationScoped.class;
	}

	@Override
	public String getName() {
		return this.beanClass.getName();
	}

	@Override
	public Set<Class<? extends Annotation>> getStereotypes() {
		return Collections.emptySet();
	}

	@Override
	public Class<T> getBeanClass() {
		return this.beanClass;
	}

	@Override
	public boolean isAlternative() {
		return false;
	}

	@Override
	public boolean isNullable() {
		return this.nullable;
	}

	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}

	@Override
	public Set<InjectionPoint> getInjectionPoints() {
		return Collections.emptySet();
	}
}
