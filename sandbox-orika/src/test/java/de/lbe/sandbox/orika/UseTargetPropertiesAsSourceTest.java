package de.lbe.sandbox.orika;

import ma.glasnost.orika.MapperFactory;

import org.junit.Test;

/**
 * @author lbeuster
 */
public class UseTargetPropertiesAsSourceTest extends AbstractOrikaTest {

	/**
     *
     */
	@Test
	public void testUseTargetPropertiesAsSourceWithMappedPropertyOnSourceBean() {

		// prepare
		TestBean source = new TestBean();
		source.setName1("source");
		TestBeanWithAdditionalProperties target = new TestBeanWithAdditionalProperties();
		target.setName1("target.name1");

		// the target bean has an additional property - but we ignore it
		MapperFactory mapperFactory = strictMapperFactory();
		mapperFactory.classMap(TestBeanWithAdditionalProperties.class, TestBean.class).field("name2", "name1").exclude("name1").byDefault().register();

		// map
		mapperFactory.getMapperFacade(TestBeanWithAdditionalProperties.class, TestBean.class, false).mapReverse(source, target);

		// name1 -> name2
		assertEquals(source.getName1(), target.getName2());
		assertEquals("target.name1", target.getName1());
	}

	/**
     *
     */
	@Test
	public void testUseTargetPropertiesAsSourceWithIgnoredPropertyOnSourceBean() {

		// prepare
		TestBean source = new TestBean();
		source.setName1("source");
		TestBeanWithAdditionalProperties target = new TestBeanWithAdditionalProperties();
		target.setName2("target.name2");

		// the target bean has an additional property - but we ignore it
		MapperFactory mapperFactory = strictMapperFactory();
		mapperFactory.classMap(TestBeanWithAdditionalProperties.class, TestBean.class).exclude("name2").byDefault().register();

		// map
		mapperFactory.getMapperFacade(TestBeanWithAdditionalProperties.class, TestBean.class, false).mapReverse(source, target);

		// assert
		assertEquals(source.getName1(), target.getName1());
		assertEquals("target.name2", target.getName2());
	}

	/**
	 * 
	 */
	public static class TestBean {

		private String name1;

		public String getName1() {
			return name1;
		}

		public void setName1(String name1) {
			this.name1 = name1;
		}
	}

	/**
	 * 
	 */
	public static class TestBeanWithAdditionalProperties {

		private String name1;

		private String name2;

		public String getName1() {
			return name1;
		}

		public void setName1(String name1) {
			this.name1 = name1;
		}

		public String getName2() {
			return name2;
		}

		public void setName2(String name2) {
			this.name2 = name2;
		}
	}
}
