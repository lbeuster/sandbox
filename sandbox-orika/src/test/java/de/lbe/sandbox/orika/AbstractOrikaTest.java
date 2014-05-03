package de.lbe.sandbox.orika;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.OrikaSystemProperties;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.impl.generator.EclipseJdtCompilerStrategy;
import ma.glasnost.orika.metadata.ClassMapBuilderFactory;

import de.asideas.lib.commons.test.junit.AbstractJUnit4Test;

/**
 * @author lbeuster
 */
public class AbstractOrikaTest extends AbstractJUnit4Test {

	/**
	 * 
	 */
	static {
		System.setProperty(OrikaSystemProperties.WRITE_SOURCE_FILES, "true");

		// Write out class files to (classpath:)/ma/glasnost/orika/generated/
		System.setProperty(OrikaSystemProperties.WRITE_CLASS_FILES, "true");

		// enable debugging
		System.setProperty(OrikaSystemProperties.COMPILER_STRATEGY, EclipseJdtCompilerStrategy.class.getName());
	}

	/**
	 * 
	 */
	MapperFactory mapperFactory(boolean useTargetPropertiesAsSource) {
		ClassMapBuilderFactory factory = new StrictUniDirectionalClassMapBuilder.Factory().useTargetPropertiesAsSource(useTargetPropertiesAsSource);
		return new DefaultMapperFactory.Builder().classMapBuilderFactory(factory).build();
	}

	/**
	 * 
	 */
	MapperFactory defaultMapperFactory() {
		return mapperFactory(false);
	}
}