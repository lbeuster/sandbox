package de.lbe.sandbox.metrics;

import static org.coursera.metrics.datadog.DatadogReporter.Expansion.COUNT;
import static org.coursera.metrics.datadog.DatadogReporter.Expansion.MEDIAN;
import static org.coursera.metrics.datadog.DatadogReporter.Expansion.P95;
import static org.coursera.metrics.datadog.DatadogReporter.Expansion.P99;
import static org.coursera.metrics.datadog.DatadogReporter.Expansion.RATE_15_MINUTE;
import static org.coursera.metrics.datadog.DatadogReporter.Expansion.RATE_1_MINUTE;

import java.io.File;
import java.util.EnumSet;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomUtils;
import org.coursera.metrics.datadog.DatadogReporter;
import org.coursera.metrics.datadog.DatadogReporter.Expansion;
import org.coursera.metrics.datadog.transport.HttpTransport;
import org.coursera.metrics.datadog.transport.UdpTransport;
import org.junit.Before;
import org.junit.Test;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.codahale.metrics.Timer.Context;

import de.asideas.ipool.commons.lib.test.junit.AbstractJUnitTest;

/**
 * @author lars.beuster
 *
 *         dns names don't work nc -zvu 10.201.225.9 8125; echo $? nc -zvu 127.0.0.1 8125; echo $?
 */
public class DatadogTest extends AbstractJUnitTest {

	private MetricRegistry registry;

	private String apiKey;

	/**
	 *
	 */
	@Before
	public void setUp() throws Exception {
		this.apiKey = FileUtils.readFileToString(new File("datadog.apikey"));
		this.registry = new MetricRegistry();

	}

	/**
	 *
	 */
	@Test
	public void testTcp() throws Exception {

		Timer timer1 = new Timer();
		registry.register("ipool.larstest.tcp[method:method1]", timer1);
		Timer timer2 = new Timer();
		registry.register("ipool.larstest.tcp[method:method2]", timer2);

		EnumSet<Expansion> expansions = EnumSet.of(COUNT, RATE_1_MINUTE, RATE_15_MINUTE, MEDIAN, P95, P99);
		HttpTransport transport = new HttpTransport.Builder().withApiKey(apiKey).build();
		DatadogReporter reporter = DatadogReporter.forRegistry(registry)/* .withEC2Host() */.withTransport(transport).withExpansions(expansions).withHost("larstest").build();

		reporter.start(5, TimeUnit.SECONDS);

		// ConsoleReporter.forRegistry(registry).build().start(5, TimeUnit.SECONDS);

		Thread thread1 = call(timer1, 500);
		Thread thread2 = call(timer2, 1000);
		thread1.join();
		thread2.join();
		reporter.close();
	}

	/**
	 *
	 */
	@Test
	public void testUdp() throws Exception {

		Timer timer1 = new Timer();
		registry.register("ipool.larstest.udp.method1", timer1);
		Timer timer2 = new Timer();
		registry.register("ipool.larstest.udp.method2", timer2);

		EnumSet<Expansion> expansions = EnumSet.of(COUNT, RATE_1_MINUTE, MEDIAN, P99);
		UdpTransport transport = new UdpTransport.Builder().withStatsdHost("tideas-ipool03").withPort(8125).build();
		DatadogReporter reporter = DatadogReporter.forRegistry(registry)/* .withEC2Host() */.withTransport(transport).withExpansions(expansions).withHost("larstest").build();

		reporter.start(5, TimeUnit.SECONDS);

		// ConsoleReporter.forRegistry(registry).build().start(5, TimeUnit.SECONDS);

		Thread thread1 = call(timer1, 500);
		Thread thread2 = call(timer2, 1000);
		thread1.join();
		thread2.join();
		reporter.close();
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
