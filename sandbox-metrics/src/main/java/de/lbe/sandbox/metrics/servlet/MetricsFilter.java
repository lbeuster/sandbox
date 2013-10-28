package de.lbe.sandbox.metrics.servlet;

import javax.servlet.annotation.WebFilter;

import com.codahale.metrics.servlet.InstrumentedFilter;

/**
 * @author lbeuster
 */
@WebFilter(urlPatterns = "/*")
public class MetricsFilter extends InstrumentedFilter {
}
