package de.lbe.sandbox.springboot;

import javax.enterprise.inject.Vetoed;
import javax.servlet.ServletContext;

import org.jboss.weld.bootstrap.api.Bootstrap;
import org.jboss.weld.environment.servlet.Listener;
import org.jboss.weld.environment.servlet.deployment.ServletDeployment;

/**
 * @author Lars Beuster
 */
@Vetoed
public class WeldListener extends Listener {

	@Override
	protected ServletDeployment createServletDeployment(ServletContext context, Bootstrap bootstrap) {
		ServletDeployment deployment = super.createServletDeployment(context, bootstrap);

		// if we want to integrate JTA
		// deployment.getServices().add(TransactionServices.class, new TransactionServicesImpl());
		return deployment;
	}
}
