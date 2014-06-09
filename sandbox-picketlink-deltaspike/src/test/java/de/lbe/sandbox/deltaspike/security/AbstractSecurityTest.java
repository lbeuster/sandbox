package de.lbe.sandbox.deltaspike.security;

import org.apache.deltaspike.security.impl.extension.SecurityInterceptor;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;

import de.asideas.lib.commons.arquillian.AbstractJUnit4ArquillianTest;
import de.asideas.lib.commons.shrinkwrap.MavenResolverUtils;
import de.asideas.lib.commons.shrinkwrap.ShrinkWrapUtils;

/**
 * @author lars.beuster
 */
public abstract class AbstractSecurityTest extends AbstractJUnit4ArquillianTest {

	/**
	 * 
	 */
	@Deployment
	public static WebArchive deployment() {
		WebArchive war = ShrinkWrapUtils.createWar();

		// activate security
		ShrinkWrapUtils.setBeansXmlWithInterceptors(war, SecurityInterceptor.class);

		MavenResolverUtils.addRuntimeDependenciesAsArchiveWithGroupIdPrefix(war, "org.apache.deltaspike.");
		MavenResolverUtils.addRuntimeDependenciesAsArchiveWithGroupIdPrefix(war, "org.picketlink");
		ShrinkWrapUtils.addFilesFromSameClasspathAndPackage(war, CustomAuthorizer.class);
		return war;
	}
}
