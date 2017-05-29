package de.lbe.sandbox.yml;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

import org.apache.commons.lang3.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Verify;

import de.asideas.ipool.commons.lib.lang.MoreStrings;
import de.asideas.ipool.commons.lib.lang.MoreThrowables;
import de.asideas.ipool.commons.lib.lang.reflect.MoreReflection;
import de.asideas.lib.commons.old.conf.ApplicationEnvironmentConfig;
import de.asideas.lib.commons.old.conf.ConfigException;
import de.asideas.lib.commons.old.conf.ConfigLoader;
import de.asideas.lib.commons.old.conf.impl.ApplicationConfigLocation;
import de.asideas.lib.commons.old.conf.impl.ApplicationConfigLocator;
import de.asideas.lib.commons.old.conf.impl.Status;

/**
 * The class is is responsible for loading one config file (or one config file group - e.g. all soacore client xmls in the publisher api).
 *
 * @author Lars Beuster
 */
public abstract class AbstractConfigLoader<T> implements ConfigLoader<T> {

	/**
	 * The logger.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractConfigLoader.class);

	/**
	 * The type of our config
	 */
	private final Class<? extends T> configClass;

	/**
	 * Our config object.
	 */
	private T config;

	/**
	 * This is the name (not a pattern) for our loaded config file(s).
	 */
	private String configFileName = null;

	/**
	 * The subconf dir location.
	 */
	private final ApplicationEnvironmentConfig applicationConfig;

	/**
	 *
	 */
	protected ApplicationConfigLocation applicationLocation;

	/**
	 * Do we want to ignore any exception while loading the config?
	 */
	private boolean ignoreAnyException = false;

	/**
	 * If optional=false then we throw an exception if the config cannot be found by the ConfigLoader. If true the config files doesn't have to exist.
	 */
	private boolean optional = false;

	/**
	 * The current status of our loader.
	 */
	private final Status status = new Status(this);

	/**
	 * @param proxyConfigClass The type of the config. If not null a proxy is created for the config object.
	 */
	protected AbstractConfigLoader(Class<? extends T> configClass, String configFileName, ApplicationEnvironmentConfig applicationConfig) {

		// check the params
		Objects.requireNonNull(configFileName, "configFileName");
		Objects.requireNonNull(applicationConfig, "applicationConfig");

		// set the values
		this.configClass = configClass;
		this.configFileName = configFileName;
		this.applicationConfig = applicationConfig;
	}

	/**
	 * This is the main method of a config loader.
	 */
	@Override
	public T load() {

		// do nothing if we are disabled
		if (this.status.isDisabled()) {
			return null;
		}

		// validate that all needed information is set
		Verify.verifyNotNull(this.applicationConfig, "applicationDir (%s)", getClass().getName());

		// we set the status in this early phase because if an exception occurs during the loading we would get into the initialized state and that
		// would prevent this loader from reloading it's config if it was changed on the filesystem
		this.status.initialized();

		try {
			this.config = loadConfig();
		} catch (IOException ex) {
			throw new ConfigException(ex);
		}
		return this.config;
	}

	/**
	 * Loads exactly on file and returnes the content as a nice object. This method does some exception handling and delegates it's call to {@link #loadFileImpl}.
	 */
	protected T loadConfig() throws IOException {

		// check if we have a application dir
		ApplicationConfigLocation applicationLocation = null;
		try {
			applicationLocation = applicationLocation();
		} catch (FileNotFoundException ex) {
			if (this.optional) {
				return null;
			}
			throw ex;
		}

		// load the config from the url
		String configFileName = getConfigFileName();
		URL fileURL = applicationLocation.locateFile(configFileName);
		T config = null;
		if (fileURL != null) {
			try {
				config = loadConfigImpl(fileURL);
			} catch (Exception ex) {
				if (!this.ignoreAnyException) {
					throw ex;
				}
				LOGGER.debug("failed to load file '{}': {}", fileURL, MoreThrowables.toStringWithoutStackTrace(ex));
			}
		}

		// do we expect a config?
		if (config == null && !this.optional) {

			// create the error message
			String className = ClassUtils.getShortClassName(getClass());
			URL applicationDirURL = applicationLocation.getURL();
			String message = MoreStrings.format("%s: File '%s' doesn't exist in '%s'", className, configFileName, applicationDirURL);
			URL defaultsURL = this.applicationLocation.getSharedURL();
			if (defaultsURL != null) {
				message = MoreStrings.format("%s or '%s'", message, defaultsURL);
			}
			throw new ConfigException(message);
		}
		return config;
	}

	/**
	 *
	 */
	protected ApplicationConfigLocation applicationLocation() throws FileNotFoundException {
		if (this.applicationLocation == null) {
			this.applicationLocation = new ApplicationConfigLocator().locate(this.applicationConfig);
		}
		return this.applicationLocation;
	}

	/**
	 * Loads one file and returns the content of the loaded file as a nice object.
	 */
	protected abstract T loadConfigImpl(URL file) throws IOException;

	/**
	 * Creates a new config object. Can be used if the config-object is created by reflection.
	 */
	protected T createConfig() {
		Class<? extends T> configClass = getConfigClass();
		Verify.verifyNotNull(configClass, "configClass");
		try {
			T config = MoreReflection.newPrivilegedInstance(configClass);
			return config;
		} catch (Exception e) {
			throw new ConfigException("failed to instantiate class '" + configClass.getName() + "'", e);
		}
	}

	@Override
	public T getConfig() {
		this.status.assertIsInitialized();
		return this.config;
	}

	protected final Class<? extends T> getConfigClass() {
		return this.configClass;
	}

	public String getConfigFileName() {
		return this.configFileName;
	}

	public void setIgnoreAnyException(boolean ignoreAnyException) {
		this.ignoreAnyException = ignoreAnyException;
	}

	public void setOptional(boolean optional) {
		this.optional = optional;
	}

	public ApplicationEnvironmentConfig getSubConfDir() {
		return this.applicationConfig;
	}

	public void setConfigFilePattern(String configFilePattern) {
		this.configFileName = configFilePattern;
	}

	public void setDisabled(boolean disabled) {
		assertIsNotInitialized();
		this.status.setDisabled(disabled);
	}

	protected void assertIsNotInitialized() {
		this.status.assertIsNotInitialized();
	}
}
