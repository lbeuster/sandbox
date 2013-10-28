package de.lbe.sandbox.metrics.webapp;

import com.codahale.metrics.health.HealthCheck;

/**
 * @author lbeuster
 */
public class MyHealthCheck extends HealthCheck {

	@Override
	public HealthCheck.Result check() throws Exception {
		return HealthCheck.Result.healthy();
	}
}