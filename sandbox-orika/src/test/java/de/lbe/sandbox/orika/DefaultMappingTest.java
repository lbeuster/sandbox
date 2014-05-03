package de.lbe.sandbox.orika;

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
		defaultMapperFactory().getMapperFacade(TestBean.class, TestBean.class, false).map(sourceBean, targetBean);

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
		defaultMapperFactory().getMapperFacade(TestBean.class, TestBean.class, false).map(source, target);

		// the target bean has now the property of the source bean
		assertNull(target.getName1());
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
