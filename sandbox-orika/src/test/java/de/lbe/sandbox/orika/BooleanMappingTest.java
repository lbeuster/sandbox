package de.lbe.sandbox.orika;

import org.junit.Test;

/**
 * @author lbeuster
 */
public class BooleanMappingTest extends AbstractOrikaTest {

	/**
     *
     */
	@SuppressWarnings({ "boxing" })
	@Test
	public void testBigBooleanWithIsGetter() throws Exception {

		// prepare
		SourceBeanWithBigBooleanProperty source = new SourceBeanWithBigBooleanProperty(Boolean.TRUE);
		TargetBeanWithBigBooleanProperty target = new TargetBeanWithBigBooleanProperty();
		target.setBooleanProperty(null);

		// map
		strictMapperFactory().getMapperFacade(SourceBeanWithBigBooleanProperty.class, TargetBeanWithBigBooleanProperty.class, false).map(source, target);

		// test
		assertNotNull(target.booleanProperty);
		assertTrue(target.booleanProperty);
	}

	/**
	 *
	 */
	public class SourceBeanWithBigBooleanProperty {

		/**
		 * It's important that we have a big Boolean.
		 */
		private Boolean booleanProperty;

		public SourceBeanWithBigBooleanProperty(Boolean booleanProperty) {
			this.booleanProperty = booleanProperty;
		}

		public Boolean isBooleanProperty() {
			return this.booleanProperty;
		}

		/**
		 * Without this method Java doesn't recognize the boolean property from the isGetter.
		 */
		public void setBooleanProperty(Boolean booleanProperty) {
			this.booleanProperty = booleanProperty;
		}
	}

	/**
	 *
	 */
	public class TargetBeanWithBigBooleanProperty {

		/**
		 * It's important that we have a big Boolean.
		 */
		Boolean booleanProperty;

		public void setBooleanProperty(Boolean value) {
			this.booleanProperty = value;
		}
	}
}
