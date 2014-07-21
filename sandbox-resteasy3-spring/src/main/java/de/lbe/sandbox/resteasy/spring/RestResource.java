package de.lbe.sandbox.resteasy.spring;

import java.beans.Introspector;
import java.util.SortedSet;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import org.springframework.beans.factory.annotation.Autowired;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.codahale.metrics.health.HealthCheckRegistry;

import de.lbe.sandbox.resteasy.spring.metrics.TestHealthCheck;

/**
 * @author lars.beuster
 */
@Path("/test")
@RestResourcePrototype
public class RestResource {

	@Context
	private UriInfo uriInfo;

	@Context
	private HttpServletRequest httpRequest;

	@Inject
	private MetricRegistry metricRegistry;

	@Inject
	private HealthCheckRegistry healthCheckRegistry;

	@Autowired
	private TestService service;

	public RestResource() {
		System.out.println(getClass().getName() + "#<init>");
	}

	@GET
	@Produces("text/plain")
	public String test() {
		return this.service.service("hallo");
	}

	@GET
	@Produces("text/plain")
	@Path("methodValidation")
	public String testMethodValidation(@NotNull @QueryParam("param") String param) {
		return param;
	}

	@GET
	@Produces("text/plain")
	@Path("metrics")
	public String testMetrics() {
		String name = RestResource.class.getName() + ".testMetrics";
		Timer timer = metricRegistry.getTimers().get(name);
		if (timer == null) {
			throw new IllegalArgumentException("timer not found in " + metricRegistry.getTimers().keySet());
		}
		return timer.toString();
	}

	@GET
	@Produces("text/plain")
	@Path("healthcheck")
	public String testHealthCheck() {
		String name = Introspector.decapitalize(TestHealthCheck.class.getSimpleName());
		SortedSet<String> names = healthCheckRegistry.getNames();
		if (!names.contains(name)) {
			throw new IllegalArgumentException("healthCheck not found in " + names);
		}
		return name;
	}
}
