package de.lbe.sandbox.orika;

import org.junit.Test;

/**
 * @author lbeuster
 */
public class AutoboxingTest extends AbstractOrikaTest {

	/**
     *
     */
	@Test
	public void testCopyIntToInteger() throws Exception {

		// prepare
		TestBeanWithPrimitiveProperty source = new TestBeanWithPrimitiveProperty();
		source.setIntValue(1234);
		TestBeanWithNonPrimitiveProperty target = new TestBeanWithNonPrimitiveProperty();

		// map
		defaultMapperFactory().getMapperFacade(TestBeanWithPrimitiveProperty.class, TestBeanWithNonPrimitiveProperty.class, false).map(source, target);

		// intValue was copied
		assertNotNull(target.getIntValue());
		assertEquals(source.getIntValue(), target.getIntValue().intValue());
	}

	/**
     *
     */
	@SuppressWarnings("boxing")
	@Test
	public void testCopyIntegerToInt() throws Exception {

		// prepare
		TestBeanWithNonPrimitiveProperty source = new TestBeanWithNonPrimitiveProperty();
		source.setIntValue(1234);
		TestBeanWithPrimitiveProperty target = new TestBeanWithPrimitiveProperty();

		// map
		defaultMapperFactory().getMapperFacade(TestBeanWithNonPrimitiveProperty.class, TestBeanWithPrimitiveProperty.class, false).map(source, target);

		// intValue was copied
		assertEquals(source.getIntValue().intValue(), target.getIntValue());
	}

	/**
    *
    */
	@Test
	public void testPrimitiveTargetSetterIsNotCalledIfSourcePropertyIsNull() throws Exception {

		// prepare
		TestBeanWithNonPrimitiveProperty source = new TestBeanWithNonPrimitiveProperty();
		source.setIntValue(null);
		TestBeanWithPrimitiveProperty target = new TestBeanWithPrimitiveProperty();
		target.setIntValue(1234);

		// map
		defaultMapperFactory().getMapperFacade(TestBeanWithNonPrimitiveProperty.class, TestBeanWithPrimitiveProperty.class, false).map(source, target);

		// intValue was copied
		assertEquals(1234, target.getIntValue());
	}

	/**
	 * 
	 */
	public static class TestBeanWithPrimitiveProperty {

		private int intValue = 0;

		public void setIntValue(int intValue) {
			this.intValue = intValue;
		}

		public int getIntValue() {
			return this.intValue;
		}
	}

	/**
	 * 
	 */
	public class TestBeanWithNonPrimitiveProperty {

		private Integer intValue = null;

		public void setIntValue(Integer intValue) {
			this.intValue = intValue;
		}

		public Integer getIntValue() {
			return this.intValue;
		}
	}
}
