package de.lbe.sandbox.orika;

import ma.glasnost.orika.DefaultFieldMapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;
import ma.glasnost.orika.metadata.ClassMapBuilderFactory;
import ma.glasnost.orika.metadata.MappingDirection;
import ma.glasnost.orika.metadata.Property;
import ma.glasnost.orika.metadata.Type;
import ma.glasnost.orika.property.PropertyResolverStrategy;

/**
 * @author lbeuster
 */
public class StrictUniDirectionalClassMapBuilder<A, B> extends ClassMapBuilder<A, B> {

	/**
	 * 
	 */
	protected StrictUniDirectionalClassMapBuilder(Type<A> aType, Type<B> bType, MapperFactory mapperFactory, PropertyResolverStrategy propertyResolver,
		DefaultFieldMapper[] defaults) {
		super(aType, bType, mapperFactory, propertyResolver, defaults);
	}

	/**
	 * 
	 */
	@Override
	public StrictUniDirectionalClassMapBuilder<A, B> byDefault(MappingDirection direction, DefaultFieldMapper... withDefaults) {

		for (final String propertyName : getPropertiesForTypeA()) {

			// already mapped?
			if (isATypePropertyAlreadyMapped(propertyName)) {
				continue;
			}

			// unknown property?
			if (!getPropertiesForTypeB().contains(propertyName)) {
				throw new UnknownPropertyException(getBType() + " doesn't contain property '" + propertyName + "'");
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

		// the property has to exist on the source side
		if (!getPropertyResolver().existsProperty(getAType(), fieldName)) {
			throw new UnknownPropertyException(fieldName);
		}

		// it original impl only does something if the property belongs to A and B - for us it's enough to have it in A
		Property aProperty = resolveProperty(getAType(), fieldName);
		return fieldMap(aProperty, aProperty, false).exclude().add();
	}

	private boolean isATypePropertyAlreadyMapped(String propertyName) {
		return getMappedPropertiesForTypeA().contains(propertyName);
	}

	/**
	 * 
	 */
	public static class Factory extends ClassMapBuilderFactory {

		@Override
		protected <A, B> ClassMapBuilder<A, B> newClassMapBuilder(Type<A> aType, Type<B> bType, MapperFactory mapperFactory, PropertyResolverStrategy propertyResolver,
			DefaultFieldMapper[] defaults) {
			return new StrictUniDirectionalClassMapBuilder<>(aType, bType, mapperFactory, propertyResolver, defaults);
		}
	}
}
