package de.lbe.sandbox.deltaspike.core;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;

import de.asideas.lib.commons.arquillian.AbstractJUnit4ArquillianTest;
import de.asideas.lib.commons.shrinkwrap.MavenResolverUtils;
import de.asideas.lib.commons.shrinkwrap.ShrinkWrapUtils;

/**
 * @author lars.beuster
 */
public abstract class AbstractCoreTest extends AbstractJUnit4ArquillianTest {

	/**
	 * 
	 */
	@Deployment
	public static WebArchive deployment() {
		WebArchive war = ShrinkWrapUtils.prepareCdiWar();
		MavenResolverUtils.addRuntimeDependenciesAsArchiveWithGroupIdPrefix(war, "org.apache.deltaspike.");
		ShrinkWrapUtils.addFilesFromSameClasspathAndPackage(war, AbstractCoreTest.class);
		return war;
	}
}
