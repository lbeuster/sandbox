package de.lbe.sandbox.metrics.webapp;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Gauge;
import com.codahale.metrics.JmxReporter;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.codahale.metrics.health.HealthCheck;
import com.codahale.metrics.health.HealthCheckRegistry;

import de.asideas.lib.commons.cdi.Startup;

/**
 * 
 */
@Path("")
@ApplicationScoped
@Startup
public class HelloWorldResource {

	@Inject
	private MetricRegistry metricRegistry;

	@Inject
	private Counter requestCounter;

	@Inject
	private Meter requestMeter;

	@Inject
	private Timer responseTimer;

	private Gauge<Long> gauge;

	@Inject
	private HealthCheckRegistry healthChecks;

	@PostConstruct
	void init() {
		this.gauge = new Gauge<Long>() {
			@Override
			public Long getValue() {
				return requestCounter.getCount();
			}
		};
		this.metricRegistry.register(MetricRegistry.name(getClass(), "gauge"), gauge);

		// add the JMX
		final JmxReporter reporter = JmxReporter.forRegistry(this.metricRegistry).inDomain("myMetrics").build();
		reporter.start();
	}

	@GET
	@Produces({ "application/json" })
	@Path("/hello")
	public Hello getHelloWorld() {
		Hello hello = new Hello();
		long start = System.currentTimeMillis();
		final Timer.Context context = responseTimer.time();
		try {
			this.requestCounter.inc();

			hello.setMessage("HALLO");
			hello.setCounter(this.requestCounter.getCount());
			hello.setMeanRequestRate(this.requestMeter.getMeanRate());
			hello.setGauge(this.gauge.getValue());

			// if called before the getMeanRate() we get a strange value
			this.requestMeter.mark();

		} finally {
			long duration = context.stop();
			hello.setDuration(duration);
		}
		System.out.println(System.currentTimeMillis() - start);
		return hello;
	}

	@GET
	@Produces({ "application/json" })
	@Path("/healthchecks")
	public Map<String, HealthCheck.Result> healthChecks() {
		final Map<String, HealthCheck.Result> results = healthChecks.runHealthChecks();
		for (Map.Entry<String, HealthCheck.Result> entry : results.entrySet()) {
			if (entry.getValue().isHealthy()) {
				System.out.println(entry.getKey() + " is healthy");
			} else {
				System.err.println(entry.getKey() + " is UNHEALTHY: " + entry.getValue().getMessage());
				final Throwable e = entry.getValue().getError();
				if (e != null) {
					e.printStackTrace();
				}
			}
		}
		return results;
	}
}
