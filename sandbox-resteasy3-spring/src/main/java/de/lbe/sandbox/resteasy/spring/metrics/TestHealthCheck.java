package de.lbe.sandbox.resteasy.spring.metrics;

import org.springframework.stereotype.Component;

import com.codahale.metrics.health.HealthCheck;

/**
 * @author lbeuster
 */
@Component
public class TestHealthCheck extends HealthCheck {

	@Override
	protected Result check() throws Exception {
		return Result.healthy();
	}
}
