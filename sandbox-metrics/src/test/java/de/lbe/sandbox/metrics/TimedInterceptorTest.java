package de.lbe.sandbox.metrics;

import javax.inject.Inject;

import org.junit.Test;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;

/**
 * @author lars.beuster
 */
public class TimedInterceptorTest extends AbstractMetricTest {

	@Inject
	private TimedTestBeanOnClassLevel timedTestBeanOnClassLevel;

	@Inject
	private TimedTestBeanOnMethodLevel timedTestBeanOnMethodLevel;

	/**
	 * 
	 */
	@Test
	public void testInterceptorOnClassLevel() {

		String timerName = MetricRegistry.name(TimedTestBeanOnClassLevel.class, "myMethod");

		// at the beginning there's no timer
		Timer timer = this.registry.getTimers().get(timerName);
		assertNull(timer);

		// execute
		this.timedTestBeanOnClassLevel.myMethod();

		// now we have a timer
		timer = this.registry.getTimers().get(timerName);
		assertNotNull(timer);
		assertEquals(1, timer.getCount());
	}

	/**
	 * 
	 */
	@Test
	public void testInterceptorOnMethodLevel() {

		String timerName = MetricRegistry.name(TimedTestBeanOnMethodLevel.class, "myMethod");

		// at the beginning there's no timer
		Timer timer = this.registry.getTimers().get(timerName);
		assertNull(timer);

		// execute
		this.timedTestBeanOnMethodLevel.myMethod();

		// now we have a timer
		timer = this.registry.getTimers().get(timerName);
		assertNotNull(timer);
		assertEquals(1, timer.getCount());
	}

	/**
	 * 
	 */
	@Timed
	public static class TimedTestBeanOnClassLevel {

		public void myMethod() {
			// no impl
		}
	}

	/**
	 * 
	 */
	public static class TimedTestBeanOnMethodLevel {

		@Timed
		public void myMethod() {
			// no impl
		}
	}
}
