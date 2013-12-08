package de.lbe.sandbox.deltaspike.security;

import javax.inject.Inject;

import org.apache.deltaspike.security.api.authorization.AccessDeniedException;
import org.apache.deltaspike.security.impl.extension.SecurityInterceptor;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;

import de.asideas.lib.commons.arquillian.AbstractJUnit4ArquillianTest;
import de.asideas.lib.commons.shrinkwrap.MavenResolverUtils;
import de.asideas.lib.commons.shrinkwrap.ShrinkWrapUtils;

/**
 * @author lars.beuster
 */
public class SecurityTest extends AbstractJUnit4ArquillianTest {

	@Inject
	SecuredService service;

	@Inject
	LoggedInUser loggedInUser;

	/**
	 * 
	 */
	@Deployment
	public static WebArchive deployment() {

		WebArchive war = ShrinkWrapUtils.createWar();
		MavenResolverUtils.addRuntimeDependenciesWithGroupIdPrefix(war, "org.apache.deltaspike.");
		ShrinkWrapUtils.setBeansXmlWithInterceptors(war, SecurityInterceptor.class);
		ShrinkWrapUtils.addFilesFromSameClasspathAndPackage(war, SecuredService.class);
		return war;
	}

	/**
	 * 
	 */
	@Test
	public void testValidationSucceeds() {
		loggedInUser.setValid(true);
		service.testRoleBasedSecurity();
	}

	/**
	 * 
	 */
	@Test(expected = AccessDeniedException.class)
	public void testValidationFails() {
		loggedInUser.setValid(false);
		service.testRoleBasedSecurity();
	}
}
