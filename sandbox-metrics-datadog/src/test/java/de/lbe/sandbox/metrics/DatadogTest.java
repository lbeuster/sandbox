package de.lbe.sandbox.metrics;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.RandomUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.codahale.metrics.Timer.Context;
import com.codahale.metrics.datadog.Datadog;
import com.codahale.metrics.datadog.DatadogReporter;
import com.codahale.metrics.datadog.DatadogReporter.Builder;

import de.asideas.lib.commons.test.junit.AbstractJUnit4Test;

/**
 * @author lars.beuster
 */
@Ignore
public class DatadogTest extends AbstractJUnit4Test {

	private MetricRegistry registry;

	private Datadog datadog;

	/**
	 * 
	 */
	@Before
	public void setUp() {
		this.registry = new MetricRegistry();
		this.datadog = new Datadog("my-api-key", "my-app-key");

	}

	/**
	 * 
	 */
	@Test
	public void testLars() throws Exception {

		Timer timer1 = new Timer();
		registry.register("celepedia.larstest.method1", timer1);
		Timer timer2 = new Timer();
		registry.register("celepedia.larstest.method2", timer2);

		Builder builder = DatadogReporter.forRegistry(registry);
		builder.build(datadog).start(5, TimeUnit.SECONDS);
		// ConsoleReporter.forRegistry(registry).build().start(5, TimeUnit.SECONDS);

		Thread thread1 = call(timer1, 500);
		Thread thread2 = call(timer2, 1000);
		thread1.join();
		thread2.join();

	}

	/**
	 * 
	 */
	private Thread call(final Timer timer, final long max) {
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						Context time = timer.time();
						Thread.sleep(RandomUtils.nextLong(100, max));
						time.close();
					} catch (Exception ex) {
						throw new RuntimeException(ex);
					}
				}
			}
		};
		Thread thread = new Thread(runnable);
		thread.setDaemon(true);
		thread.start();
		return thread;
	}
}
