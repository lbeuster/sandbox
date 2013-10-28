package de.lbe.sandbox.metrics;

import javax.inject.Inject;

import org.junit.Test;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;

/**
 * @author lars.beuster
 */
public class MeteredInterceptorTest extends AbstractMetricTest {

	@Inject
	private MeteredTestBeanOnClassLevel meteredTestBeanOnClassLevel;

	@Inject
	private MeteredTestBeanOnMethodLevel meteredTestBeanOnMethodLevel;

	/**
	 * 
	 */
	@Test
	public void testInterceptorOnClassLevel() {

		String meterName = MetricRegistry.name(MeteredTestBeanOnClassLevel.class, "myMethod");

		// at the beginning there's no timer
		Meter meter = this.registry.getMeters().get(meterName);
		assertNull(meter);

		// execute
		this.meteredTestBeanOnClassLevel.myMethod();

		// now we have a timer
		meter = this.registry.getMeters().get(meterName);
		assertNotNull(meter);
		assertEquals(1, meter.getCount());
	}

	/**
	 * 
	 */
	@Test
	public void testInterceptorOnMethodLevel() {

		String meterName = MetricRegistry.name(MeteredTestBeanOnMethodLevel.class, "myMethod");

		// at the beginning there's no timer
		Meter meter = this.registry.getMeters().get(meterName);
		assertNull(meter);

		// execute
		this.meteredTestBeanOnMethodLevel.myMethod();

		// now we have a timer
		meter = this.registry.getMeters().get(meterName);
		assertNotNull(meter);
		assertEquals(1, meter.getCount());
	}

	/**
	 * 
	 */
	@Metered
	public static class MeteredTestBeanOnClassLevel {

		public void myMethod() {
			// no impl
		}
	}

	/**
	 * 
	 */
	public static class MeteredTestBeanOnMethodLevel {

		@Metered
		public void myMethod() {
			// no impl
		}
	}
}
