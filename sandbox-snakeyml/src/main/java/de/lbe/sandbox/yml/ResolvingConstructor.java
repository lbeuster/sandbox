package de.lbe.sandbox.yml;

import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.ScalarNode;

import de.lbe.lib.commons.old.util.PropertiesResolver;

/**
 * @author lars.beuster
 */
public class ResolvingConstructor extends Constructor {

	private final PropertiesResolver propertiesResolver;

	public ResolvingConstructor(PropertiesResolver propertiesResolver) {
		this.propertiesResolver = propertiesResolver;
	}

	@Override
	protected Object constructScalar(ScalarNode node) {
		String value = node.getValue();
		value = this.propertiesResolver.resolveValue(value);
		return value;
	}
}
