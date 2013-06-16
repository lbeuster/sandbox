package de.lbe.weld;

import javax.enterprise.inject.spi.Bean;

import org.jboss.weld.bootstrap.BeanDeployer;
import org.jboss.weld.ejb.EjbDescriptors;
import org.jboss.weld.manager.BeanManagerImpl;
import org.jboss.weld.resources.ClassTransformer;

import com.zanox.lib.commons.cdi.BeanManagerUtils;
import com.zanox.lib.commons.lang.Assert;

import de.lbe.weld.test.SingletonBean;

/**
 * @author lars.beuster
 */
public class WeldBeanManagerUtils extends BeanManagerUtils {

	/**
	 *
	 */
	private static <T> void addBean(BeanManagerImpl beanManager, Bean<T> bean) {

		// check the params
		Assert.isNotNull(beanManager, "beanManager");
		Assert.isNotNull(bean, "bean");

		// add
		beanManager.addBean(bean);

		// the bean list is cached - invalidate
		beanManager.getBeanResolver().clear();
	}

	/**
	 *
	 */
	public static <T> void addBeanClass(BeanManagerImpl beanManager, Class<T> beanClass) {

		// check the params
		Assert.isNotNull(beanManager, "beanManager");
		Assert.isNotNull(beanClass, "beanClass");

		// prepare
		ClassTransformer classTransformer = beanManager.getServices().get(ClassTransformer.class);
		Assert.isNotNullState(classTransformer, "classTransformer is null");

		// load the metadata
		BeanDeployer beanDeployer = new BeanDeployer(beanManager, EjbDescriptors.EMPTY, beanManager.getServices());
		beanDeployer.addClass(beanClass.getName());
		beanDeployer.createBeans();
		beanDeployer.deploy();
		
		// the bean list is cached - invalidate
		beanManager.getBeanResolver().clear();
	}

	/**
	 *
	 */
	@SuppressWarnings("unchecked")
	public static <T> void addSingletonBean(BeanManagerImpl beanManager, T instance, Class<T> beanType) {

		// check the params
		Assert.isNotNull(beanManager, "beanManager");
		if (beanType == null) {
			Assert.isNotNull(instance, "instance");
			beanType = (Class<T>) instance.getClass();
		}

		// add
		SingletonBean<T> bean = new SingletonBean<T>(instance, beanType);
		addBean(beanManager, bean);
	}

	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	public static <T> void addSingletonBean(BeanManagerImpl beanManager, T instance) {
		Assert.isNotNull(instance, "instance");
		addSingletonBean(beanManager, instance, (Class<T>) instance.getClass());
	}
}
