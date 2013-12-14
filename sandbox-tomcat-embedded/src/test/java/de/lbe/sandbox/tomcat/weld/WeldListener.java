package de.lbe.sandbox.tomcat.weld;

import javax.servlet.ServletContext;

import org.jboss.weld.bootstrap.api.Bootstrap;
import org.jboss.weld.environment.servlet.Listener;
import org.jboss.weld.environment.servlet.deployment.ServletDeployment;
import org.jboss.weld.transaction.spi.TransactionServices;

import de.lbe.sandbox.tomcat.weld.spi.TransactionServicesImpl;

/**
 * Meant as an embedded tomcat for integration tests.
 * 
 * @author Lars Beuster
 */
public class WeldListener extends Listener {

	protected ServletDeployment createServletDeployment(ServletContext context, Bootstrap bootstrap) {
		ServletDeployment deployment = super.createServletDeployment(context, bootstrap);
		deployment.getServices().add(TransactionServices.class, new TransactionServicesImpl());
		return deployment;
	}

}
