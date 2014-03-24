package de.lbe.sandbox.orika;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingException;

import org.junit.Test;

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

		MapperFactory mapperFactory = defaultMapperFactory();
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
		MapperFactory mapperFactory = defaultMapperFactory();
		mapperFactory.classMap(TestBean.class, TestBean.class).exclude("invalidName");
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
}
