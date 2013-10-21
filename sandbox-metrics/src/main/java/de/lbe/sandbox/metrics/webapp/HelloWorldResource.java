package de.lbe.sandbox.metrics.webapp;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;

/**
 * 
 */
@Path("")
@RequestScoped
public class HelloWorldResource {

	@Inject
	private MetricRegistry metricRegistry;

	private Counter requestCounter;

	private Meter requestMeter;

	private Timer responseTimer;

	@PostConstruct
	void init() {
		this.requestCounter = metricRegistry.counter(MetricRegistry.name(getClass(), "requestCounter"));
		this.requestMeter = metricRegistry.meter(MetricRegistry.name(getClass(), "requests"));
		this.responseTimer = metricRegistry.timer(MetricRegistry.name(getClass(), "responses"));
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

			// if called before the getMeanRate() we get a strange value
			this.requestMeter.mark();

		} finally {
			long duration = context.stop();
			hello.setDuration(duration);
		}
		System.out.println(System.currentTimeMillis() - start);
		return hello;
	}
}
