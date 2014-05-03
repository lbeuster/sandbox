package de.lbe.sandbox.orika;

import java.util.Set;

import ma.glasnost.orika.DefaultFieldMapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingException;
import ma.glasnost.orika.metadata.ClassMapBuilder;
import ma.glasnost.orika.metadata.ClassMapBuilderFactory;
import ma.glasnost.orika.metadata.Property;
import ma.glasnost.orika.metadata.Type;
import ma.glasnost.orika.property.PropertyResolverStrategy;

/**
 * @author lbeuster
 */
public class StrictUniDirectionalClassMapBuilder<A, B> extends ClassMapBuilder<A, B> {

	private final boolean useTargetPropertiesAsSource;

	/**
	 * <p>
	 * This is a hack. The default behavior of the ClassMapBuilder is if a property doesn't exist on both sides it is simply ignored. So there's no need to exclude a property that
	 * is only on one side - and if we do it anyway an exception is thrown.
	 * </p>
	 * <p>
	 * But we want to be strict. If a property exists only on the source side we throw an exception. That's why we have to exclude the property. But this fails because of the
	 * default impl. That's why we have this hack.
	 * </p>
	 */
	private boolean inExclude = false;

	/**
	 * 
	 */
	protected StrictUniDirectionalClassMapBuilder(Type<A> aType, Type<B> bType, MapperFactory mapperFactory, PropertyResolverStrategy propertyResolver,
		DefaultFieldMapper[] defaults, boolean useTargetPropertiesAsSource) {
		super(aType, bType, mapperFactory, propertyResolver, defaults);
		this.useTargetPropertiesAsSource = useTargetPropertiesAsSource;
	}

	/**
	 * 
	 */
	@Override
	public StrictUniDirectionalClassMapBuilder<A, B> byDefault(DefaultFieldMapper... withDefaults) {

		for (final String propertyName : getSourceProperties()) {

			// already mapped?
			if (isSourcePropertyAlreadyMapped(propertyName)) {
				continue;
			}

			// unknown property?
			if (!getTargetProperties().contains(propertyName)) {
				throw new UnknownPropertyException(getTargetType() + " doesn't contain property '" + propertyName + "'");
			}

			// ignore getClass() mapping
			if (propertyName.equals("class")) {
				continue;
			}

			// add the field
			fieldMap(propertyName, true).add();
		}
		return this;
	}

	/**
	 * 
	 */
	@Override
	public ClassMapBuilder<A, B> exclude(String fieldName) {
		inExclude = true;
		try {
			return super.exclude(fieldName);
		} finally {
			inExclude = false;
		}
	}

	/**
	 * 
	 */
	@Override
	protected Property resolveProperty(java.lang.reflect.Type type, String expr) {
		try {
			return super.resolveProperty(type, expr);
		} catch (MappingException ex) {
			if (inExclude) {
				// the default impl calls resolveProperty() 2 times - for the source and for the target. The property has to exist at least on one of both. So it's not valid if the
				// property doesn't exist on both sides.
				inExclude = false;
				return new Property.Builder().expression(expr).name(expr).getter("<doesn_t-matter>").build();
			}
			throw ex;
		}
	}

	private Type<?> getTargetType() {
		return this.useTargetPropertiesAsSource ? getAType() : getBType();
	}

	private Set<String> getSourceProperties() {
		return this.useTargetPropertiesAsSource ? getPropertiesForTypeB() : getPropertiesForTypeA();
	}

	private Set<String> getAlreadyMappedSourceProperties() {
		return this.useTargetPropertiesAsSource ? getMappedPropertiesForTypeB() : getMappedPropertiesForTypeA();
	}

	private boolean isSourcePropertyAlreadyMapped(String propertyName) {
		return getAlreadyMappedSourceProperties().contains(propertyName);
	}

	private Set<String> getTargetProperties() {
		return this.useTargetPropertiesAsSource ? getPropertiesForTypeA() : getPropertiesForTypeB();
	}

	/**
	 * 
	 */
	public static class Factory extends ClassMapBuilderFactory {

		private boolean useTargetPropertiesAsSource = false;

		@Override
		protected <A, B> ClassMapBuilder<A, B> newClassMapBuilder(Type<A> aType, Type<B> bType, MapperFactory mapperFactory, PropertyResolverStrategy propertyResolver,
			DefaultFieldMapper[] defaults) {
			return new StrictUniDirectionalClassMapBuilder<>(aType, bType, mapperFactory, propertyResolver, defaults, this.useTargetPropertiesAsSource);
		}

		public Factory useTargetPropertiesAsSource(boolean useTargetPropertiesAsSource) {
			this.useTargetPropertiesAsSource = useTargetPropertiesAsSource;
			return this;
		}
	}
}
