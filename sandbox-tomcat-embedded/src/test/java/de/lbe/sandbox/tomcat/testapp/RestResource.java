package de.lbe.sandbox.tomcat.testapp;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.transaction.UserTransaction;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import de.asideas.lib.commons.lang.Assert;

/**
 * @author lars.beuster
 */
@Path("/rest")
@ApplicationScoped
public class RestResource {

	@Inject
	BeanManager beanManager;

	@Inject
	UserTransaction tx;

	@GET
	@Produces("text/plain")
	public String service() throws Exception {
		Assert.isNotNull(beanManager, "beanManager");
		Assert.isNotNull(tx, "tx");

		try {
			tx.begin();
			return "HALLO";
		} finally {
			tx.commit();
		}

	}
}
