package de.lbe.sandbox.metrics;

import javax.inject.Inject;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;

import org.junit.Test;

import com.codahale.metrics.health.HealthCheck;
import com.codahale.metrics.health.HealthCheckRegistry;

/**
 * @author lars.beuster
 */
public class HealthCheckInitializerTest extends AbstractMetricTest {

	@Inject
	private HealthCheckRegistry healthCheckRegistry;

	/**
	 * 
	 */
	@Test
	public void testAutoStartHealthChecks() {
		String name = "simpleHealthCheck";
		assertThat(this.healthCheckRegistry.getNames(), hasItem(name));
	}

	/**
	 * 
	 */
	@Test
	public void testExplicitName() {
		String name = "explicitName";
		assertThat(this.healthCheckRegistry.getNames(), hasItem(name));
	}

	/**
	 * 
	 */
	@Test
	public void testIgnorehealthChecksWithoutQualifier() {
		String name = "ignoredHealthCheck";
		assertThat(this.healthCheckRegistry.getNames(), not(hasItem(name)));
	}

	/**
	 * 
	 */
	@HealthChecked
	public static class SimpleHealthCheck extends HealthCheck {

		@Override
		public HealthCheck.Result check() throws Exception {
			return HealthCheck.Result.healthy();
		}
	}

	/**
	 * 
	 */
	@HealthChecked("explicitName")
	public static class HealthCheckWithName extends HealthCheck {

		@Override
		public HealthCheck.Result check() throws Exception {
			return HealthCheck.Result.healthy();
		}
	}

	/**
	 * 
	 */
	public static class IgnoredHealthCheck extends HealthCheck {

		IgnoredHealthCheck() {
			throw new UnsupportedOperationException();
		}

		@Override
		public HealthCheck.Result check() throws Exception {
			return HealthCheck.Result.healthy();
		}
	}
}
