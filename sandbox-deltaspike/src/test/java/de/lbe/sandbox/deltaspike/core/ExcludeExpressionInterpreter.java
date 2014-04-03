package de.lbe.sandbox.deltaspike.core;

import javax.enterprise.inject.Vetoed;

import org.apache.deltaspike.core.api.interpreter.ExpressionInterpreter;

@Vetoed
public class ExcludeExpressionInterpreter implements ExpressionInterpreter<String, Boolean> {

	@Override
	public Boolean evaluate(String expression) {
		System.out.println(expression);
		return false;
	}
}
