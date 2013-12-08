package de.lbe.sandbox.tomcat;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.el.ELContextListener;
import javax.enterprise.inject.spi.BeanManager;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.jsp.JspApplicationContext;
import javax.servlet.jsp.JspFactory;

import org.jboss.logging.Logger;
import org.jboss.weld.bootstrap.api.Bootstrap;
import org.jboss.weld.bootstrap.api.Environment;
import org.jboss.weld.bootstrap.api.Environments;
import org.jboss.weld.bootstrap.api.Service;
import org.jboss.weld.environment.Container;
import org.jboss.weld.environment.ContainerContext;
import org.jboss.weld.environment.gwtdev.GwtDevHostedModeContainer;
import org.jboss.weld.environment.jetty.JettyContainer;
import org.jboss.weld.environment.servlet.Listener;
import org.jboss.weld.environment.servlet.deployment.ServletDeployment;
import org.jboss.weld.environment.servlet.deployment.URLScanner;
import org.jboss.weld.environment.servlet.deployment.VFSURLScanner;
import org.jboss.weld.environment.servlet.services.ServletResourceInjectionServices;
import org.jboss.weld.environment.servlet.util.Reflections;
import org.jboss.weld.environment.servlet.util.ServiceLoader;
import org.jboss.weld.environment.tomcat.Tomcat6Container;
import org.jboss.weld.environment.tomcat7.Tomcat7Container;
import org.jboss.weld.injection.spi.ResourceInjectionServices;
import org.jboss.weld.manager.api.WeldManager;
import org.jboss.weld.servlet.api.ServletListener;
import org.jboss.weld.servlet.api.helpers.ForwardingServletListener;
import org.jboss.weld.transaction.spi.TransactionServices;

/**
 * Meant as an embedded tomcat for integration tests.
 * 
 * @author Lars Beuster
 */
public class MyWeldBootstrap extends ForwardingServletListener {
	private static final Logger log = Logger.getLogger(Listener.class);

	private static final String BOOTSTRAP_IMPL_CLASS_NAME = "org.jboss.weld.bootstrap.WeldBootstrap";
	private static final String WELD_LISTENER_CLASS_NAME = "org.jboss.weld.servlet.WeldListener";
	private static final String EXPRESSION_FACTORY_NAME = "org.jboss.weld.el.ExpressionFactory";
	public static final String BEAN_MANAGER_ATTRIBUTE_NAME = Listener.class.getPackage().getName() + "." + BeanManager.class.getName();

	private final transient Bootstrap bootstrap;
	private final transient ServletListener weldListener;
	private Container container;

	public MyWeldBootstrap() {
		try {
			bootstrap = Reflections.newInstance(BOOTSTRAP_IMPL_CLASS_NAME);
		} catch (IllegalArgumentException e) {
			throw new IllegalStateException("Error loading Weld bootstrap, check that Weld is on the classpath", e);
		}
		try {
			weldListener = Reflections.newInstance(WELD_LISTENER_CLASS_NAME);
		} catch (IllegalArgumentException e) {
			throw new IllegalStateException("Error loading Weld listener, check that Weld is on the classpath", e);
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		bootstrap.shutdown();

		if (container != null) {
			container.destroy(new ContainerContext(sce, null));
		}

		super.contextDestroyed(sce);
	}

	/**
	 * Create server deployment.
	 * <p/>
	 * Can be overridden with custom servlet deployment. e.g. exact resources listing in restricted env like GAE
	 * 
	 * @param context the servlet context
	 * @param bootstrap the bootstrap
	 * @return new servlet deployment
	 */
	protected ServletDeployment createServletDeployment(ServletContext context, Bootstrap bootstrap) {
		return new ServletDeployment(context, bootstrap);
	}

	/**
	 * Get appropriate scanner. Return null to leave it to defaults.
	 * 
	 * @param classLoader the classloader
	 * @param context the servlet context
	 * @return custom url scanner or null if we should use default
	 */
	protected URLScanner createUrlScanner(ClassLoader classLoader, ServletContext context) {
		try {
			classLoader.loadClass("org.jboss.virtual.VFS"); // check if we can use JBoss VFS
			return new VFSURLScanner(classLoader);
		} catch (Throwable t) {
			return null;
		}
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {

		ClassLoader classLoader = Reflections.getClassLoader();
		ServletContext context = sce.getServletContext();

		URLScanner scanner = createUrlScanner(classLoader, context);
		if (scanner != null) {
			context.setAttribute(URLScanner.class.getName(), scanner);
		}

		ServletDeployment deployment = createServletDeployment(context, bootstrap);
		try {
			deployment.getWebAppBeanDeploymentArchive().getServices().add(ResourceInjectionServices.class, new ServletResourceInjectionServices() {
			});
		} catch (NoClassDefFoundError e) {
			// Support GAE
			log.warn("@Resource injection not available in simple beans");
		}

		EnvironmentImpl env = new EnvironmentImpl();
		env.getRequiredBeanDeploymentArchiveServices().addAll(Environments.SERVLET.getRequiredBeanDeploymentArchiveServices());
		env.getRequiredDeploymentServices().addAll(Environments.SERVLET.getRequiredDeploymentServices());
		env.addRequiredDeploymentService(TransactionServices.class);

		bootstrap.startContainer(env, deployment).startInitialization();
		WeldManager manager = bootstrap.getManager(deployment.getWebAppBeanDeploymentArchive());

		ContainerContext cc = new ContainerContext(sce, manager);
		StringBuilder dump = new StringBuilder();
		Container container = findContainer(cc, dump);
		if (container == null) {
			log.info("No supported servlet container detected, CDI injection will NOT be available in Servlets, Filters or Listeners");
			log.debugv("Exception dump from Container lookup: {0}", dump);
		} else {
			container.initialize(cc);
			this.container = container;
		}

		// Push the manager into the servlet context so we can access in JSF
		context.setAttribute(BEAN_MANAGER_ATTRIBUTE_NAME, manager);

		if (JspFactory.getDefaultFactory() != null) {
			JspApplicationContext jspApplicationContext = JspFactory.getDefaultFactory().getJspApplicationContext(context);

			// Register the ELResolver with JSP
			jspApplicationContext.addELResolver(manager.getELResolver());

			// Register ELContextListener with JSP
			jspApplicationContext.addELContextListener(Reflections.<ELContextListener> newInstance("org.jboss.weld.el.WeldELContextListener"));

			// Push the wrapped expression factory into the servlet context so that Tomcat or Jetty can hook it in using a container code
			context.setAttribute(EXPRESSION_FACTORY_NAME, manager.wrapExpressionFactory(jspApplicationContext.getExpressionFactory()));
		}

		bootstrap.deployBeans().validateBeans().endInitialization();
		super.contextInitialized(sce);
	}

	@Override
	protected ServletListener delegate() {
		return weldListener;
	}

	/**
	 * Find container env.
	 * 
	 * @param cc the container context
	 * @param dump the exception dump
	 * @return valid container or null
	 */
	protected Container findContainer(ContainerContext cc, StringBuilder dump) {
		Iterable<Container> extContainers = ServiceLoader.load(Container.class, getClass().getClassLoader());
		Container container = checkContainers(cc, dump, extContainers);
		if (container == null) {
			container = checkContainers(cc, dump, Arrays.asList(
			// Needs to be first: gwt-dev jar has tomcat classes but uses jetty
				GwtDevHostedModeContainer.INSTANCE, Tomcat7Container.INSTANCE, Tomcat6Container.INSTANCE, JettyContainer.INSTANCE));
		}
		return container;
	}

	protected Container checkContainers(ContainerContext cc, StringBuilder dump, Iterable<Container> containers) {
		for (Container c : containers) {
			try {
				if (c.touch(cc)) {
					return c;
				}
			} catch (Throwable t) {
				dump.append(c).append("->").append(t.getMessage()).append("\n");
			}
		}
		return null;
	}

	public static class EnvironmentImpl implements Environment {

		private final Set<Class<? extends Service>> requiredDeploymentServices;

		private final Set<Class<? extends Service>> requiredBeanDeploymentArchiveServices;

		EnvironmentImpl() {
			this.requiredBeanDeploymentArchiveServices = new HashSet<Class<? extends Service>>();
			this.requiredDeploymentServices = new HashSet<Class<? extends Service>>();
		}

		public Set<Class<? extends Service>> getRequiredBeanDeploymentArchiveServices() {
			return requiredBeanDeploymentArchiveServices;
		}

		public Set<Class<? extends Service>> getRequiredDeploymentServices() {
			return requiredDeploymentServices;
		}

		private EnvironmentImpl addRequiredDeploymentService(Class<? extends Service> service) {
			this.requiredDeploymentServices.add(service);
			return this;
		}

		private EnvironmentImpl addRequiredBeanDeploymentArchiveService(Class<? extends Service> service) {
			this.requiredBeanDeploymentArchiveServices.add(service);
			return this;
		}

	}

}
