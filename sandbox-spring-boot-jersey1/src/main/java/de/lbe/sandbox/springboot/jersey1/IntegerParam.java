package de.lbe.sandbox.springboot.jersey1;

import de.asideas.ipool.commons.lib.jaxrs.core.params.AbstractParam;
import de.asideas.ipool.commons.lib.util.convert.TypeConverter;

/**
 * @author lars.beuster
 */
public class IntegerParam extends AbstractParam<Integer> {

	public IntegerParam(String input) {
		super(input);
	}

	@Override
	protected Integer parse(String input) throws Exception {
		return TypeConverter.toInt(input);
	}
}
