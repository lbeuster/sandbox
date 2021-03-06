package de.lbe.sandbox.orika;

import ma.glasnost.orika.MapperFactory;

import org.junit.Test;

/**
 * @author Lars Beuster
 */
public class DefaultMappingTest extends AbstractOrikaTest {

	/**
     *
     */
	@Test
	public void testCopyToBeanOfSameClass() throws Exception {

		// prepare
		TestBean sourceBean = new TestBean();
		sourceBean.setName1("name.1");
		sourceBean.setName2("name.2");
		TestBean targetBean = new TestBean();

		// copy
		strictMapperFactory().getMapperFacade(TestBean.class, TestBean.class, false).map(sourceBean, targetBean);

		// the target bean has now the property of the source bean
		assertEquals(sourceBean.getName1(), targetBean.getName1());
		assertEquals(sourceBean.getName2(), targetBean.getName2());
	}

	/**
    *
    */
	@Test
	public void testSourcePropertyIsNull() throws Exception {

		// prepare
		TestBean source = new TestBean();
		source.setName1(null);
		TestBean target = new TestBean();
		target.setName1("name.1");

		// copy
		strictMapperFactory().getMapperFacade(TestBean.class, TestBean.class, false).map(source, target);

		// the target bean has now the property of the source bean
		assertNull(target.getName1());
	}

	/**
     *
     */
	@Test
	public void testIgnoreNullSourceValues() {

		// prepare
		TestBean source = new TestBean();
		source.setName1(null);
		source.setName2(null);
		TestBean target = new TestBean();
		target.setName1("name1");
		target.setName2("name2");

		// prepare - the mapNull has to be before the mapping
		MapperFactory mapperFactory = strictMapperFactory();
		mapperFactory.classMap(TestBean.class, TestBean.class).field("name1", "name1").mapNulls(false).field("name2", "name2").byDefault().register();

		// map
		mapperFactory.getMapperFacade(TestBean.class, TestBean.class, false).map(source, target);

		// assert
		assertNull("name1", target.getName1());
		assertEquals("name2", target.getName2());
	}

	/**
	 * 
	 */
	public static class TestBean {

		private String name1 = null;

		private String name2 = null;

		public void setName1(String name1) {
			this.name1 = name1;
		}

		public String getName1() {
			return this.name1;
		}

		public void setName2(String name2) {
			this.name2 = name2;
		}

		public String getName2() {
			return this.name2;
		}
	}
}
