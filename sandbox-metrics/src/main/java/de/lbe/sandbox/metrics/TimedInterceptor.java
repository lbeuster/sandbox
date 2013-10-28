package de.lbe.sandbox.metrics;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.codahale.metrics.Timer.Context;

/**
 * @author lbeuster
 */
@Interceptor
@Timed
public class TimedInterceptor {

	private static final Logger LOGGER = LoggerFactory.getLogger(TimedInterceptor.class);

	@Inject
	private MetricRegistry metricRegistry;

	/**
	 * 
	 */
	@AroundInvoke
	public Object aroundInvoke(InvocationContext ctx) throws Exception {

		// get the annotation
		Timed timed = ctx.getMethod().getAnnotation(Timed.class);
		if (timed == null) {
			timed = ctx.getTarget().getClass().getAnnotation(Timed.class);
		}

		// we should have an annotation now
		if (timed == null) {
			LOGGER.warn("no " + Timed.class.getName() + " annotation found on method " + ctx.getMethod());
			return ctx.proceed();
		}

		// execute with timer
		String timerName = MetricNameUtils.forInterceptedMethod(ctx.getMethod());
		Timer timer = metricRegistry.timer(timerName);
		Context timerCtx = timer.time();
		try {
			return ctx.proceed();
		} finally {
			timerCtx.stop();
		}
	}
}
