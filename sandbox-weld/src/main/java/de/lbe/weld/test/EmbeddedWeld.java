package de.lbe.weld.test;

import java.util.Collection;
import java.util.Collections;

import javax.enterprise.inject.spi.Extension;

import org.jboss.weld.bootstrap.WeldBootstrap;
import org.jboss.weld.bootstrap.api.Bootstrap;
import org.jboss.weld.bootstrap.api.Environments;
import org.jboss.weld.bootstrap.api.ServiceRegistry;
import org.jboss.weld.bootstrap.api.helpers.SimpleServiceRegistry;
import org.jboss.weld.bootstrap.spi.BeanDeploymentArchive;
import org.jboss.weld.bootstrap.spi.BeansXml;
import org.jboss.weld.bootstrap.spi.Deployment;
import org.jboss.weld.bootstrap.spi.Metadata;
import org.jboss.weld.bootstrap.spi.Scanning;
import org.jboss.weld.ejb.spi.EjbDescriptor;
import org.jboss.weld.manager.BeanManagerImpl;
import org.jboss.weld.metadata.BeansXmlImpl;
import org.jboss.weld.metadata.ScanningImpl;

import de.asideas.lib.commons.lang.Assert;
import de.lbe.weld.WeldBeanManagerUtils;

/**
 * @author lars.beuster
 */
public class EmbeddedWeld {

	/**
	 * 
	 */
	private WeldBootstrap bootstrap = null;

	/**
	 * 
	 */
	private BeanManagerImpl beanManager = null;

	/**
	 * 
	 */
	public BeanManagerImpl start() {

		// check the state
		Assert.isNull(this.bootstrap, "already started");

		// create an empty deployment archive
		WeldBootstrap bootstrap = new WeldBootstrap();
		BeanDeploymentArchive emptyDeploymentArchive = new EmptyDeploymentArchive(String.valueOf(hashCode()));
		EmptyDeployment deployment = new EmptyDeployment(bootstrap, emptyDeploymentArchive);

		// start weld
		bootstrap.startContainer(Environments.SE, deployment);
		bootstrap.startInitialization();
		bootstrap.deployBeans();
		bootstrap.validateBeans();
		bootstrap.endInitialization();

		// init our local variables
		this.beanManager = bootstrap.getManager(emptyDeploymentArchive);
		Assert.isNotNullState(this.beanManager, "bootstrap.beanManager");
		this.bootstrap = bootstrap;
		return this.beanManager;
	}

	/**
	 * 
	 */
	public void stop() {
		Assert.isNotNull(this.bootstrap, "not started");
		this.bootstrap.shutdown();
		this.bootstrap = null;
		this.beanManager = null;
	}

	/**
	 * 
	 */
	public BeanManagerImpl getBeanManager() {
		Assert.isNotNull(this.beanManager, "not started");
		return this.beanManager;
	}

	public <T> void addBeanClass(Class<T> beanClass) {
		WeldBeanManagerUtils.addBeanClass(getBeanManager(), beanClass);
	}

	public void addSingletonBean(Object instance) {
		WeldBeanManagerUtils.addSingletonBean(getBeanManager(), instance);
	}

	public <T> void addSingletonBean(T instance, Class<T> beanType) {
		WeldBeanManagerUtils.addSingletonBean(getBeanManager(), instance, beanType);
	}

	public <T> T getBean(Class<T> beanType) {
		return WeldBeanManagerUtils.getContextualReference(getBeanManager(), beanType);
	}

	/**
	 * 
	 */
	private class EmptyDeployment implements Deployment {

		private final ServiceRegistry serviceRegistry;
		private final BeanDeploymentArchive deploymentArchive;
		private final Collection<BeanDeploymentArchive> deploymentArchives;
		private final Iterable<Metadata<Extension>> extensions;

		public EmptyDeployment(Bootstrap bootstrap, BeanDeploymentArchive deploymentArchive) {
			this.serviceRegistry = new SimpleServiceRegistry();
			this.deploymentArchive = deploymentArchive;
			this.deploymentArchives = Collections.singleton(deploymentArchive);
			this.extensions = bootstrap.loadExtensions(getClassLoader());
		}

		private ClassLoader getClassLoader() {
			final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			return classLoader != null ? classLoader : getClass().getClassLoader();
		}

		@Override
		public ServiceRegistry getServices() {
			return this.serviceRegistry;
		}

		@Override
		public Iterable<Metadata<Extension>> getExtensions() {
			return this.extensions;
		}

		@Override
		public Collection<BeanDeploymentArchive> getBeanDeploymentArchives() {
			return this.deploymentArchives;
		}

		@Override
		public BeanDeploymentArchive loadBeanDeploymentArchive(Class<?> beanClass) {
			return this.deploymentArchive;
		}
	}

	/**
	 * 
	 */
	private class EmptyDeploymentArchive implements BeanDeploymentArchive {

		private final ServiceRegistry serviceRegistry;
		private final String id;
		private final BeansXml beansXml;

		@SuppressWarnings("unchecked")
		public EmptyDeploymentArchive(String id) {
			this.id = id;
			this.serviceRegistry = new SimpleServiceRegistry();
			Scanning scanning = new ScanningImpl(Collections.EMPTY_LIST, Collections.EMPTY_LIST);
			this.beansXml =
				new BeansXmlImpl(Collections.EMPTY_LIST, Collections.EMPTY_LIST, Collections.EMPTY_LIST, Collections.EMPTY_LIST, scanning);

		}

		@Override
		public String getId() {
			return this.id;
		}

		@Override
		public Collection<EjbDescriptor<?>> getEjbs() {
			return Collections.emptyList();
		}

		@Override
		public ServiceRegistry getServices() {
			return this.serviceRegistry;
		}

		@Override
		public Collection<BeanDeploymentArchive> getBeanDeploymentArchives() {
			return Collections.emptySet();
		}

		@Override
		public Collection<String> getBeanClasses() {
			return Collections.emptySet();
		}

		@Override
		public BeansXml getBeansXml() {
			return this.beansXml;
		}
	}
}
