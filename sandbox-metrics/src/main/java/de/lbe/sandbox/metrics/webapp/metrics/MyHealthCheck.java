package de.lbe.sandbox.metrics.webapp.metrics;

import com.codahale.metrics.health.HealthCheck;

/**
 * @author lbeuster
 */
public class MyHealthCheck extends HealthCheck {

	@Override
	public HealthCheck.Result check() throws Exception {
		long time = System.currentTimeMillis();
		if (time % 2 == 0) {
			return HealthCheck.Result.healthy();
		} else {
			return HealthCheck.Result.unhealthy("Unhealthy: " + time);
		}
	}
}