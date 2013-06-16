package de.lbe.weld.test;

import javax.enterprise.context.spi.CreationalContext;

/**
 * @author lars.beuster
 */
public class SingletonBean<T> extends AbstractConfigurableBean<T> {

	/**
	 * 
	 */
	private final T instance;

	/**
	 * 
	 */
	public SingletonBean(T instance, Class<T> beanClass) {
		super(beanClass);
		this.instance = instance;
		setNullable(this.instance == null);
	}

	@Override
	public T create(CreationalContext<T> creationalContext) {
		return this.instance;
	}
}
