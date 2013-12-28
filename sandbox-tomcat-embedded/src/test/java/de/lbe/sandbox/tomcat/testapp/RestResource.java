package de.lbe.sandbox.tomcat.testapp;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.transaction.UserTransaction;
import javax.validation.Valid;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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

	@Inject
	TestService service;

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

	@POST
	@Produces("application/json")
	public TestBean validate(@Valid TestBean testBean) {
		return testBean;

		// TestService1 service = BeanManagerUtils.getContextualReference(CDI.current().getBeanManager(), TestService1.class);
		// return service.service(testBean);
	}
}
