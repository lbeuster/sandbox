package de.lbe.orika;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

/**
 * Orika says that we should use the mapper factory as a singleton.
 * 
 * http://orika-mapper.github.io/orika-docs/performance-tuning.html
 * 
 * @author lars.beuster
 */
public class SingletonMapperFactory {

	private static MapperFactory mapperFactory;

	static {
		mapperFactory = new DefaultMapperFactory.Builder().build();
	}

	public static MapperFactory get() {
		return mapperFactory;
	}
}
