package de.lbe.sandbox.yml;

import java.util.Properties;

import de.asideas.lib.commons.old.conf.ApplicationEnvironmentConfig;
import de.asideas.lib.commons.old.conf.ConfigException;
import de.asideas.lib.commons.old.conf.SettingsProperties;
import de.asideas.lib.commons.old.conf.loaders.properties.ChainedResolvablePropertiesProvider;
import de.asideas.lib.commons.old.conf.loaders.properties.DefaultResolvablePropertiesProvider;
import de.asideas.lib.commons.old.conf.loaders.properties.ResolvablePropertiesProvider;
import de.asideas.lib.commons.old.conf.loaders.properties.ResolvableSettingsPropertiesLoaderProvider;
import de.asideas.lib.commons.old.conf.loaders.properties.ResolvableSettingsPropertiesProvider;
import de.lbe.lib.commons.old.util.PropertiesResolver;

/**
 * @author Lars Beuster
 */
public abstract class AbstractPropertiesResolvingConfigLoader<T> extends AbstractConfigLoader<T> {

	/**
	 * Should we resolve ${...}-notations in the (xml) files against our settings properties?
	 */
	private Boolean resolveValues = null;

	/**
	 * The properties that are used to resolve the xml-values.
	 */
	private ResolvablePropertiesProvider resolvablePropertiesProvider = null;

	protected AbstractPropertiesResolvingConfigLoader(Class<? extends T> configClass, String configFileName, ApplicationEnvironmentConfig subConfDir) {
		super(configClass, configFileName, subConfDir);
	}

	/**
	 *
	 */
	public void addResolvablePropertiesProvider(ResolvablePropertiesProvider provider) {
		if (this.resolvablePropertiesProvider == null) {
			setResolvablePropertiesProvider(provider);
		} else {
			ResolvablePropertiesProvider newProvider = new ChainedResolvablePropertiesProvider(this.resolvablePropertiesProvider, provider);
			setResolvablePropertiesProvider(newProvider);
		}
	}

	/**
	 * Creates a properties resolver. This method os the preferred way to create a resolver.
	 */
	protected PropertiesResolver createPropertiesResolver() {
		if (!isResolveValues()) {
			throw new ConfigException("Cannot create properties resolver since no properties are set.");
		}
		Properties properties = getResolvableProperties();
		return new PropertiesResolver(properties);
	}

	/**
	 * These are the properties that are used to resolve ${...} values. Can be null.
	 */
	protected Properties getResolvableProperties() {
		if (this.resolvablePropertiesProvider != null) {
			return this.resolvablePropertiesProvider.getProperties();
		}
		return null;
	}

	private void setResolvablePropertiesProvider(ResolvablePropertiesProvider provider) {
		this.resolvablePropertiesProvider = provider;
	}

	public void addResolvableProperties(Properties properties) {
		addResolvablePropertiesProvider(new DefaultResolvablePropertiesProvider(properties));
	}

	public void addResolvableSettingsProperties(SettingsProperties settingsProperties) {
		addResolvablePropertiesProvider(new ResolvableSettingsPropertiesProvider(settingsProperties));
	}

	public void addResolvableSettingsPropertiesLoader(SettingsPropertiesLoader<?> settingsPropertiesLoader) {
		addResolvablePropertiesProvider(new ResolvableSettingsPropertiesLoaderProvider(settingsPropertiesLoader));
	}

	public boolean isResolveValues() {
		return this.resolveValues != null ? this.resolveValues.booleanValue() : this.resolvablePropertiesProvider != null;
	}

	public void setResolveValues(boolean resolveValues) {
		this.resolveValues = Boolean.valueOf(resolveValues);
	}
}
