package de.lbe.sandbox.orika;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingException;

import org.junit.Test;

import de.lbe.sandbox.orika.UseTargetPropertiesAsSourceTest.TestBeanWithAdditionalProperties;

/**
 * @author lbeuster
 */
public class IgnorePropertyTest extends AbstractOrikaTest {

	/**
     *
     */
	@Test
	public void testIgnoreSourceProperty() throws Exception {

		// prepare
		String targetName1 = "targetName";
		TestBean sourceBean = new TestBean();
		TestBean targetBean = new TestBean();
		targetBean.setName(targetName1);

		MapperFactory mapperFactory = strictMapperFactory();
		mapperFactory.classMap(TestBean.class, TestBean.class).exclude("name").byDefault().register();

		// copy
		mapperFactory.getMapperFacade(TestBean.class, TestBean.class, false).map(sourceBean, targetBean);

		// the target bean has still it's old property
		assertEquals(targetName1, targetBean.getName());
	}

	/**
     *
     */
	@Test(expected = MappingException.class)
	public void testIgnoreInvalidProperty() throws Exception {
		MapperFactory mapperFactory = strictMapperFactory();
		mapperFactory.classMap(TestBean.class, TestBean.class).exclude("invalidName");
	}

	/**
	 * 
	 */
	@Test
	public void testTargetHasMorePropertiesThanSource() {

		// prepare
		TestBean source = new TestBean();
		source.setName("name");
		TestBeanWithAdditionalProperty target = new TestBeanWithAdditionalProperty();

		// copy
		strictMapperFactory().getMapperFacade(TestBean.class, TestBeanWithAdditionalProperty.class, false).map(source, target);

		// assert
		assertEquals(source.getName(), target.getName());
	}

	/**
	 * 
	 */
	@Test
	public void testSourceHasMorePropertiesThanTargetSuccess() {

		// prepare
		TestBeanWithAdditionalProperty source = new TestBeanWithAdditionalProperty();
		source.setName("name");
		TestBean target = new TestBean();

		// exclude
		MapperFactory mapperFactory = strictMapperFactory();
		mapperFactory.classMap(TestBeanWithAdditionalProperty.class, TestBean.class).exclude("name2").byDefault().register();

		// copy
		mapperFactory.getMapperFacade(TestBeanWithAdditionalProperty.class, TestBean.class, false).map(source, target);

		// assert
		assertEquals(source.getName(), target.getName());
	}

	/**
	 * 
	 */
	@Test(expected = UnknownPropertyException.class)
	public void testSourceHasMorePropertiesThanTargetFailure() {

		// prepare
		TestBeanWithAdditionalProperties source = new TestBeanWithAdditionalProperties();
		TestBean target = new TestBean();

		// copy
		strictMapperFactory().getMapperFacade(TestBeanWithAdditionalProperties.class, TestBean.class, false).map(source, target);
	}

	/**
	 * 
	 */
	public static class TestBean {

		private String name = null;

		public void setName(String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}
	}

	/**
	 * 
	 */
	public static class TestBeanWithAdditionalProperty {

		private String name = null;

		private String name2;

		public void setName(String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}

		public String getName2() {
			return name2;
		}

		public void setName2(String name2) {
			this.name2 = name2;
		}
	}
}
