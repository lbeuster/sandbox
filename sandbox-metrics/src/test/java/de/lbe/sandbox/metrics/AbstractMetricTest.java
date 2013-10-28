package de.lbe.sandbox.metrics;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;

import com.codahale.metrics.MetricRegistry;

import de.asideas.lib.commons.arquillian.AbstractJUnit4ArquillianTest;
import de.asideas.lib.commons.cdi.Startup;
import de.asideas.lib.commons.shrinkwrap.ShrinkWrapUtils;

/**
 * @author lars.beuster
 */
public abstract class AbstractMetricTest extends AbstractJUnit4ArquillianTest {

	@Inject
	protected MetricRegistry registry;

	/**
	 * 
	 */
	@Deployment
	public static WebArchive deployment() {
		WebArchive archive = ShrinkWrapUtils.createWar();
		ShrinkWrapUtils.setBeansXmlWithInterceptors(archive, TimedInterceptor.class);
		ShrinkWrapUtils.addFilesFromSameClasspathAndPackage(archive, MetricNameUtils.class, false);
		ShrinkWrapUtils.addArchiveOfClass(archive, Startup.class);
		// archive.addClass(MetricRegistryProducer.class);
		// archive.addClass(MetricProducer.class);
		return archive;
	}
}
