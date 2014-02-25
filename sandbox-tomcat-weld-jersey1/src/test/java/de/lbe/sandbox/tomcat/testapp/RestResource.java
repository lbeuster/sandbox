package de.lbe.sandbox.tomcat.testapp;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import de.asideas.lib.commons.lang.Assert;

/**
 * @author lars.beuster
 */
@Path("/rest")
@ApplicationScoped
@SetResponseHeader
public class RestResource {

	@Inject
	BeanManager beanManager;

	@Inject
	TestService service;

	@Context
	private UriInfo uriInfo;

	@GET
	@Produces("text/plain")
	public String service() throws Exception {
		Assert.isNotNull(beanManager, "beanManager");
		return "HALLO";
	}

	@POST
	@Produces("application/json")
	public TestBean validate(@Valid TestBean testBean) {
		return testBean;
	}

	@GET
	@Produces("application/json")
	@Path("/testInjection")
	public TestBean testInjection() {
		TestBean bean = new TestBean();
		bean.setCdiActive(this.beanManager != null);
		service.service(null);
		return bean;
	}

	@GET
	@Produces("application/json")
	@Path("/testContextInjection")
	public TestBean testContextInjection() {
		TestBean bean = new TestBean();
		bean.setContextInjectionActive(this.uriInfo != null);
		service.service(null);
		return bean;
	}

	@GET
	@Produces("application/json")
	@Path("/testContextInjectionInCdiBean")
	public TestBean testContextInjectionInCdiBean() {
		TestBean bean = new TestBean();
		bean.setContextInjectionActive(this.service.getUriInfo() != null);
		return bean;
	}
}
