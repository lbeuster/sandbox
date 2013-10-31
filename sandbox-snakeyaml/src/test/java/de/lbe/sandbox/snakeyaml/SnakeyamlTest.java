package de.lbe.sandbox.snakeyaml;

import org.junit.Test;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeId;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;
import org.yaml.snakeyaml.resolver.Resolver;

import de.asideas.lib.commons.test.junit.AbstractJUnit4Test;
import de.asideas.lib.commons.util.PropertiesResolver;

/**
 * @author lars.beuster
 */
public class SnakeyamlTest extends AbstractJUnit4Test {

	/**
	 * 
	 */
	@Test
	public void testParse() {

		System.setProperty("test.property", "15");

		Yaml yaml = new Yaml(new MyConstructor(), new Representer(), new DumperOptions(), new Resolver());
		String document = "hello: ${test.property}";
		TestBean bean = yaml.loadAs(document, TestBean.class);
		System.out.println(bean.getHello());
	}

	/**
	 * 
	 */
	@Test
	public void testUnknownProperty() {

		Yaml yaml = new Yaml();
		String document = "unknown: test";
		TestBean bean = yaml.loadAs(document, TestBean.class);
		System.out.println(bean.getHello());
	}

	/**
	 * 
	 */
	public static class MyConstructor extends Constructor {

		PropertiesResolver propertiesResolver;

		public MyConstructor() {
			propertiesResolver = new PropertiesResolver(System.getProperties());
		}

		protected Object constructScalar(ScalarNode node) {
			String value = node.getValue();
			value = propertiesResolver.resolveValue((String) value);
			return value;
		}

		@Override
		protected Object constructObject(Node node) {
			Object value = super.constructObject(node);
			// if (value instanceof String) {
			// value = propertiesResolver.resolveValue((String) value);
			// }
			return value;
		}
	}

	/**
	 *
	 */
	public static class MyResolver extends Resolver {

		PropertiesResolver propertiesResolver;

		public MyResolver() {
			propertiesResolver = new PropertiesResolver(System.getProperties());
		}

		@Override
		public Tag resolve(NodeId kind, String value, boolean implicit) {
			// value = propertiesResolver.resolveValue(value);
			return super.resolve(kind, value, implicit);
		}
	}

	/**
	 * 
	 */
	public static class TestBean {
		private int hello;

		public int getHello() {
			return this.hello;
		}

		public void setHello(int hello) {
			this.hello = hello;
		}
	}
}
