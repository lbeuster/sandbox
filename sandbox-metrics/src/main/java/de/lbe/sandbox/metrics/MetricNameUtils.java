package de.lbe.sandbox.metrics;

import java.beans.Introspector;
import java.lang.reflect.Member;

import javax.enterprise.inject.spi.InjectionPoint;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.health.HealthCheck;

import de.asideas.lib.commons.lang.StringUtils;

/**
 * @author lbeuster
 */
public class MetricNameUtils {

	/**
	 * 
	 */
	public static String forInjectionPoint(InjectionPoint ip) {
		Member member = ip.getMember();
		Class<?> cls = member.getDeclaringClass();
		String memberName = member.getName();
		return MetricRegistry.name(cls, memberName);
	}

	/**
	 * 
	 */
	public static String forHealthCheck(HealthCheck healthCheck) {

		String name = null;

		// first check the annotation for a name
		HealthChecked annotation = healthCheck.getClass().getAnnotation(HealthChecked.class);
		if (annotation != null) {
			name = annotation.value();
		}

		// if not found use the class name (starting with a lower letter).
		if (StringUtils.isEmpty(name)) {
			name = healthCheck.getClass().getSimpleName();
			name = Introspector.decapitalize(name);
		}
		return name;
	}
}
