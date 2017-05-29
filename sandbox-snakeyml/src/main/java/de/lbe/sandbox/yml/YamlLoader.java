package de.lbe.sandbox.yml;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.yaml.snakeyaml.Yaml;

import de.asideas.lib.commons.old.conf.ApplicationEnvironmentConfig;
import de.asideas.lib.commons.old.conf.impl.yaml.ResolvingConstructor;
import de.lbe.lib.commons.old.util.PropertiesResolver;

/**
 * @author lars.beuster
 */
public class YamlLoader<T> extends AbstractPropertiesResolvingConfigLoader<T> {

	public YamlLoader(Class<? extends T> configClass, String configFileName, ApplicationEnvironmentConfig subConfDir) {
		super(configClass, configFileName, subConfDir);
	}

	/**
     *
     */
	@Override
	protected T loadConfigImpl(URL file) throws IOException {
		Yaml parser = createYamlParser();
		InputStream in = file.openStream();
		try {
			return parser.loadAs(in, getConfigClass());
		} finally {
			IOUtils.closeQuietly(in);
		}
	}

	/**
	 *
	 */
	protected Yaml createYamlParser() {
		if (!isResolveValues()) {
			return new Yaml();
		}

		// we want to resolve
		PropertiesResolver propertiesResolver = createPropertiesResolver();
		ResolvingConstructor constructor = new ResolvingConstructor(propertiesResolver);
		return new Yaml(constructor);
	}
}
