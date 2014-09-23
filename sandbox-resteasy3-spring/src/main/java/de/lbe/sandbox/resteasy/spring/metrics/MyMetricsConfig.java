package de.lbe.sandbox.resteasy.spring.metrics;

import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;
import com.ryantenney.metrics.spring.config.annotation.MetricsConfigurerAdapter;

import de.asideas.lib.commons.metrics.spring.config.MetricsConfiguration;

@Configuration
// @EnableMetrics
@Import(MetricsConfiguration.class)
public class MyMetricsConfig extends MetricsConfigurerAdapter {

	@Override
	public void configureReporters(MetricRegistry metricRegistry) {
		ConsoleReporter.forRegistry(metricRegistry).build().start(1, TimeUnit.MINUTES);
	}
}