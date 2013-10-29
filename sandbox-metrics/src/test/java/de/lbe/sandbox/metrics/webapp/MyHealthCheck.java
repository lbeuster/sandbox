package de.lbe.sandbox.metrics.webapp;

import com.codahale.metrics.health.HealthCheck;

import de.lbe.sandbox.metrics.health.HealthChecked;

/**
 * @author lbeuster
 */
@HealthChecked
public class MyHealthCheck extends HealthCheck {

	@Override
	public HealthCheck.Result check() throws Exception {
		return HealthCheck.Result.healthy();
	}
}