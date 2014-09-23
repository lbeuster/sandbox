package de.lbe.sandbox.metrics;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.RandomUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.codahale.metrics.Timer.Context;
import com.codahale.metrics.graphite.Graphite;
import com.codahale.metrics.graphite.GraphiteReporter;
import com.codahale.metrics.graphite.GraphiteReporter.Builder;

import de.asideas.lib.commons.test.junit.AbstractJUnit4Test;

/**
 * @author lars.beuster
 */
@Ignore
public class GraphiteTest extends AbstractJUnit4Test {

	private static final String API_KEY = "TheApiKey";

	private MetricRegistry registry;

	private Graphite graphite;

	/**
	 * 
	 */
	@Before
	public void setUp() {
		this.registry = new MetricRegistry();
		this.graphite = new Graphite(new InetSocketAddress("carbon.hostedgraphite.com", 2003));
	}

	/**
	 * 
	 */
	@Test
	public void testLars() throws Exception {

		Timer timer1 = registry.timer("celepedia.larstest3.method");
		Timer timer2 = registry.timer("celepedia.larstest1.method2");

		// SuffixMetricFilter filter =
		// new SuffixMetricFilter().excludeSuffixes("p50", "p75", "p95", "p98", "p99", "p999", "stddev", "min", "max", "m15_rate", "m1_rate", "m5_rate", "mean_rate");
		Builder builder = GraphiteReporter.forRegistry(registry).prefixedWith(API_KEY);
		// builder = builder.filter(filter);
		builder.build(graphite).start(5, TimeUnit.SECONDS);
		// ConsoleReporter.forRegistry(registry).build().start(5, TimeUnit.SECONDS);

		Thread thread1 = call(timer1, 500);
		// Thread thread2 = call(timer2, 1000);
		thread1.join();
		// thread2.join();

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
						Context context = timer.time();
						Thread.sleep(RandomUtils.nextLong(100, max));
						context.close();
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
