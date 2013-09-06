package de.lbe.orika;

import ma.glasnost.orika.BoundMapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.OrikaSystemProperties;
import ma.glasnost.orika.impl.DefaultMapperFactory;

import org.junit.Test;

import de.asideas.lib.commons.test.junit.AbstractJUnit4Test;

/**
 * Orika say that we should use the
 * 
 * @author lars.beuster
 */
public class OrikaTest extends AbstractJUnit4Test {

	/**
	 * 
	 */
	@Test
	public void testSimpleMapping() {

		MapperFactory m;

		// prepare
		TestBean source = new TestBean();
		source.setName("source");
		TestBean target = new TestBean();

		// map
		MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
		BoundMapperFacade<TestBean, TestBean> mapper = mapperFactory.getMapperFacade(TestBean.class, TestBean.class, false);
		mapper.map(source, target);

		// assert
		assertEquals("source", target.getName());
	}

	/**
	 * 
	 */
	@Test
	public void testSourceHasMorePropertiesThanTarget() {

		// prepare
		TestBeanWithMoreProperties source = new TestBeanWithMoreProperties();
		source.setName("name");
		source.setAdditionalProperty("source");
		TestBean target = new TestBean();

		// map
		MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
		BoundMapperFacade<TestBeanWithMoreProperties, TestBean> mapper =
			mapperFactory.getMapperFacade(TestBeanWithMoreProperties.class, TestBean.class, false);
		mapper.map(source, target);

		// unfortunatly we get here
	}

	/**
	 * 
	 */
	@Test
	public void testTargetHasMorePropertiesThanTarget() {

		System.setProperty(OrikaSystemProperties.WRITE_SOURCE_FILES, "true");

		// Write out class files to (classpath:)/ma/glasnost/orika/generated/
		System.setProperty(OrikaSystemProperties.WRITE_CLASS_FILES, "true");

		// System.setProperty(OrikaSystemProperties.COMPILER_STRATEGY, EclipseJdtCompilerStrategy.class.getName());

		// prepare
		TestBean source = new TestBean();
		source.setName("name");
		TestBeanWithMoreProperties target = new TestBeanWithMoreProperties();

		// map
		MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

		mapperFactory.classMap(TestBean.class, TestBeanWithMoreProperties.class).byDefault().register();

		BoundMapperFacade<TestBean, TestBeanWithMoreProperties> mapper =
			mapperFactory.getMapperFacade(TestBean.class, TestBeanWithMoreProperties.class, false);
		mapper.map(source, target);

		// unfortunatly we get here
	}

	/**
	 * 
	 */
	public static class TestBean {
		private String name;

		public String getName() {
			return this.name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}

	/**
	 * 
	 */
	public static class TestBeanWithMoreProperties extends TestBean {

		private String additionalProperty;

		public String getAdditionalProperty() {
			return this.additionalProperty;
		}

		public void setAdditionalProperty(String additionalProperty) {
			this.additionalProperty = additionalProperty;
		}
	}
}
