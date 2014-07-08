package de.lbe.sandbox.deltaspike.scheduler;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.JavaArchive;

import de.asideas.lib.commons.arquillian.AbstractJUnit4ArquillianTest;
import de.asideas.lib.commons.arquillian.deltaspike.TestDeltaspikeProducer;
import de.asideas.lib.commons.shrinkwrap.ShrinkWrapUtils;

/**
 * @author lars.beuster
 */
public abstract class AbstractSchedulerTest extends AbstractJUnit4ArquillianTest {

	/**
	 * 
	 */
	@Deployment
	public static JavaArchive deployment() {
		JavaArchive jar = ShrinkWrapUtils.prepareCdiJar();
		jar.addClass(TestDeltaspikeProducer.class);
		ShrinkWrapUtils.addFilesFromSameClasspathAndPackage(jar, CdiAwareQuartzJob.class, true);
		return jar;
	}
}
