package de.lbe.sandbox.tomcat.testapp;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

/**
 * @author lars.beuster
 */
@Path("/rest")
@ApplicationScoped
public class RestResource {

	@Inject
	BeanManager beanManager;

	@Inject
	TestService service;

	public RestResource() {
		System.out.println(getClass().getName() + "#<init>");
	}

	@GET
	@Produces("application/json")
	@Path("{name}")
	public TestBean validate(@PathParam("name") String name) {
		TestBean bean = new TestBean();
		bean.setName(name);
		return bean;
	}

	@GET
	@Produces("application/json")
	@Path("/testInjection")
	public TestBean testInjection() {
		TestBean bean = new TestBean();
		bean.setCdiActive(this.beanManager != null);
		return bean;
	}

	@POST
	@Produces("application/json")
	public TestBean validate(@Valid TestBean testBean) {
		return testBean;

		// TestService1 service = BeanManagerUtils.getContextualReference(CDI.current().getBeanManager(), TestService1.class);
		// return service.service(testBean);
	}
}
