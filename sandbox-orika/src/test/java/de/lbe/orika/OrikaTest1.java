package de.lbe.orika;

import ma.glasnost.orika.BoundMapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

import org.junit.Test;

import com.zanox.lib.commons.test.junit.AbstractJUnit4Test;

/**
 * Orika say that we should use the
 * 
 * @author lars.beuster
 */
public class OrikaTest1 extends AbstractJUnit4Test {

	/**
	 * 
	 */
	@Test
	public void testSimpleMapping() {

		MapperFactory m;

		// prepare
		SourceBean source = new SourceBean();
		source.setName1("source");
		TargetBean target = new TargetBean();

		// map
		MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

		mapperFactory.classMap(SourceBean.class, TargetBean.class).mapNulls(false).byDefault().register();

		BoundMapperFacade<SourceBean, TargetBean> mapper = mapperFactory.getMapperFacade(SourceBean.class, TargetBean.class, false);
		mapper.map(source, target);

		// assert
		assertEquals("source", target.getName2());
	}

	/**
	 * 
	 */
	public static class SourceBean {

		private String name1;

		public String getName1() {
			return this.name1;
		}

		public void setName1(String name1) {
			this.name1 = name1;
		}
	}

	/**
	 * 
	 */
	public static class TargetBean {

		private String name2;

		public String getName2() {
			return this.name2;
		}

		public void setName2(String name2) {
			this.name2 = name2;
		}
	}
}
