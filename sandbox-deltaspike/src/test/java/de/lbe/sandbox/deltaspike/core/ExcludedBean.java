package de.lbe.sandbox.deltaspike.core;

import org.apache.deltaspike.core.api.exclude.Exclude;

/**
 * @author lbeuster
 */
// @Exclude(onExpression = "myExpression", interpretedBy = ExcludeExpressionInterpreter.class)
@Exclude(onExpression = "sun.cpu.endian==little")
public class ExcludedBean {

	// no impl

}
