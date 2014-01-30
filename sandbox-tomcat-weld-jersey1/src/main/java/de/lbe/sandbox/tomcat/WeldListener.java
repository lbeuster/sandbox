package de.lbe.sandbox.tomcat;

import javax.servlet.ServletContext;

import org.jboss.weld.bootstrap.api.Bootstrap;
import org.jboss.weld.environment.servlet.Listener;
import org.jboss.weld.environment.servlet.deployment.ServletDeployment;

/**
 * Meant as an embedded tomcat for integration tests.
 * 
 * @author Lars Beuster
 */
public class WeldListener extends Listener {

	@Override
	protected ServletDeployment createServletDeployment(ServletContext context, Bootstrap bootstrap) {
		ServletDeployment deployment = super.createServletDeployment(context, bootstrap);
		return deployment;
	}

}
