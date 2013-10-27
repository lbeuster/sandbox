package de.lbe.sandbox.metrics;

import java.util.Map;

import javax.inject.Inject;

import static org.hamcrest.Matchers.hasValue;

import org.junit.Test;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Meter;
import com.codahale.metrics.Timer;

import static de.asideas.lib.commons.test.hamcrest.Matchers.mapWithSize;

/**
 * @author lars.beuster
 */
public class MetricProducerTest extends AbstractMetricTest {

	@Inject
	private Timer timer1;

	@Inject
	private Timer timer2;

	@Inject
	private Counter counter1;

	@Inject
	private Counter counter2;

	@Inject
	private Meter meter1;

	@Inject
	private Meter meter2;

	/**
	 * 
	 */
	@Test
	public void testProducedTimer() throws Exception {

		assertNotNull(this.registry);
		Map<String, Timer> timers = this.registry.getTimers();

		// assert
		assertNotEquals(this.timer1, this.timer2);
		assertThat(timers, mapWithSize(2));
		assertThat(timers, hasValue(this.timer1));
		assertThat(timers, hasValue(this.timer2));
	}

	/**
	 * 
	 */
	@Test
	public void testProducedCounter() throws Exception {

		assertNotNull(this.registry);
		Map<String, Counter> counters = this.registry.getCounters();

		// assert
		assertNotEquals(this.counter1, this.counter2);
		assertThat(counters, mapWithSize(2));
		assertThat(counters, hasValue(this.counter1));
		assertThat(counters, hasValue(this.counter2));
	}

	/**
	 * 
	 */
	@Test
	public void testProducedMeter() throws Exception {

		assertNotNull(this.registry);
		Map<String, Meter> meters = this.registry.getMeters();

		// assert
		assertNotEquals(this.meter1, this.meter2);
		assertThat(meters, mapWithSize(2));
		assertThat(meters, hasValue(this.meter1));
		assertThat(meters, hasValue(this.meter2));
	}
}
