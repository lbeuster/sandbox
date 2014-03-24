package de.lbe.sandbox.orika;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingException;

import org.junit.Test;

/**
 * @author lbeuster
 */
public class MapPropertyNamesTest extends AbstractOrikaTest {

	/**
     *
     */
	@Test
	public void testMapPropertyName() throws Exception {

		// prepare
		SourceBean source = new SourceBean();
		source.setName1("name.1");
		TargetBean target = new TargetBean();
		target.setName1("target.name.1");

		// prepare mapper
		MapperFactory mapperFactory = defaultMapperFactory();
		// mapperFactory = new DefaultMapperFactory.Builder().build();

		mapperFactory.classMap(SourceBean.class, TargetBean.class).field("name1", "name2").byDefault().register();

		// map
		mapperFactory.getMapperFacade(SourceBean.class, TargetBean.class, false).map(source, target);

		// the name2-property of the target bean has now the value of the name1-property of the source bean
		assertEquals(source.getName1(), target.getName2());
		assertEquals("target.name.1", target.getName1());
	}

	/**
     *
     */
	@Test(expected = MappingException.class)
	public void testMapToInvalidPropertyName() throws Exception {

		// prepare mapper
		MapperFactory mapperFactory = defaultMapperFactory();
		mapperFactory.classMap(SourceBean.class, TargetBean.class).field("name1", "invalid-name");
	}

	/**
	 * 
	 */
	public static class SourceBean {

		private String name1 = null;

		public void setName1(String name1) {
			this.name1 = name1;
		}

		public String getName1() {
			return this.name1;
		}
	}

	/**
	 * 
	 */
	public static class TargetBean {

		private String name1;

		private String name2;

		public String getName2() {
			return name2;
		}

		public void setName2(String name2) {
			this.name2 = name2;
		}

		public String getName1() {
			return name1;
		}

		public void setName1(String name1) {
			this.name1 = name1;
		}
	}
}
