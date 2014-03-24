package de.lbe.sandbox.orika;

import org.junit.Test;

/**
 * @author lbeuster
 */
public class TargetBeanWithoutGetterTest extends AbstractOrikaTest {

	/**
    *
    */
	@Test
	public void testTargetBeanWithoutGetter() {

		// prepare
		SourceTestBean source = new SourceTestBean();
		TargetTestBean target = new TargetTestBean();
		source.setName("NAME1");

		// copy
		defaultMapperFactory().getMapperFacade(SourceTestBean.class, TargetTestBean.class, false).map(source, target);

		// test
		assertEquals(source.getName(), target.name);
	}

	/**
	 * 
	 */
	public static class SourceTestBean {

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
	public static class TargetTestBean {

		String name = null;

		public void setName(String name) {
			this.name = name;
		}
	}
}
