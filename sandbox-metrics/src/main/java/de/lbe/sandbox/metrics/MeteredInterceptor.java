package de.lbe.sandbox.metrics;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;

/**
 * @author lbeuster
 */
@Interceptor
@Metered
public class MeteredInterceptor {

	private static final Logger LOGGER = LoggerFactory.getLogger(MeteredInterceptor.class);

	@Inject
	private MetricRegistry metricRegistry;

	/**
	 * 
	 */
	@AroundInvoke
	public Object aroundInvoke(InvocationContext ctx) throws Exception {

		// get the annotation
		Metered metered = ctx.getMethod().getAnnotation(Metered.class);
		if (metered == null) {
			metered = ctx.getTarget().getClass().getAnnotation(Metered.class);
		}

		// we should have an annotation now
		if (metered != null) {
			String meterName = MetricNameUtils.forInterceptedMethod(ctx.getMethod());
			Meter meter = metricRegistry.meter(meterName);
			meter.mark();
		} else {
			LOGGER.warn("no " + Metered.class.getName() + " annotation found on method " + ctx.getMethod());
		}

		// execute
		return ctx.proceed();
	}
}
