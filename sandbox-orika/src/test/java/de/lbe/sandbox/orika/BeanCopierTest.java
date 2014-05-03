package de.lbe.sandbox.orika;

import static org.hamcrest.Matchers.hasSize;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import de.asideas.lib.commons.beans.copy.AbstractCollectionConverter;
import de.asideas.lib.commons.beans.copy.AbstractSimplePropertyConverter;
import de.asideas.lib.commons.beans.copy.BeanCopier;
import de.asideas.lib.commons.beans.copy.BeanCopyException;
import de.asideas.lib.commons.test.junit.AbstractJUnit4Test;

/**
 * @author lbeuster
 */
public class BeanCopierTest extends AbstractJUnit4Test {

	/**
     *
     */
	@Test
	public void testCopyListPropertyOfSimpleType() {

		String listElement = "hallo";

		// prepare the objects
		TestBeanWithStringListProperty sourceBean = new TestBeanWithStringListProperty();
		sourceBean.setListProperty(Collections.singletonList(listElement));
		TestBeanWithStringListProperty targetBean = new TestBeanWithStringListProperty();

		// prepare the copier
		BeanCopier<TestBeanWithStringListProperty, TestBeanWithStringListProperty> copier = new BeanCopier<>();
		copier.setUseSimpleCollectionConversion("listProperty");

		// copy
		copier.copy(sourceBean, targetBean);

		List<String> targetList = targetBean.getListProperty();
		assertThat(targetList, hasSize(1));
		assertEquals(listElement, targetList.get(0));
	}

	/**
     *
     */
	@Test
	public void testCopyListPropertyAlwaysFailsWithoutExplicitConverter() {

		String element = "hallo";

		// prepare the objects
		TestBeanWithStringListProperty sourceBean = new TestBeanWithStringListProperty();
		sourceBean.setListProperty(Collections.singletonList(element));
		TestBeanWithStringListProperty targetBean = new TestBeanWithStringListProperty();

		// copy always fails without explicit converter for lists
		BeanCopier<TestBeanWithStringListProperty, TestBeanWithStringListProperty> copier = new BeanCopier<>();
		try {
			copier.copy(sourceBean, targetBean);
			fail("collection conversion not supported");
		} catch (BeanCopyException ex) {
			// expected
		}

		// but if we add an explcit converter everything is fine
		copier.setUseSimpleCollectionConversion("listProperty");
		copier.copy(sourceBean, targetBean);
		assertSame(sourceBean.getListProperty().get(0), targetBean.getListProperty().get(0));
	}

	/**
	 * In this test the target bean has a similar interface as generated jaxb-stubs - no setter for collections.
	 */
	@Test
	public void testUseAddAllTargetCollectionGetterOnSingleProperty() {

		String listElement = "hallo";

		// prepare the objects
		TestBeanWithStringListProperty sourceBean = new TestBeanWithStringListProperty();
		sourceBean.setListProperty(Collections.singletonList(listElement));
		TestBeanWithListPropertyGetter targetBean = new TestBeanWithListPropertyGetter();

		// prepare the copier
		BeanCopier<TestBeanWithStringListProperty, TestBeanWithListPropertyGetter> copier = new BeanCopier<>();
		copier.setUseTargetPropertiesAsSource(true);
		copier.setUseTargetCollectionGetterWithAddAll("listProperty");
		copier.setUseSimpleCollectionConversion("listProperty");

		// copy
		copier.copy(sourceBean, targetBean);

		List<String> targetList = targetBean.getListProperty();
		assertThat(targetList, hasSize(1));
		assertEquals(listElement, targetList.get(0));
	}

	/**
	 * In this test the target bean has a similar interface as generated jaxb-stubs - no setter for collections.
	 */
	@Test
	public void testUseAllAllTargetCollectionGetterOnAllProperties() {

		String listElement = "hallo";

		// prepare the objects
		TestBeanWithStringListProperty sourceBean = new TestBeanWithStringListProperty();
		sourceBean.setListProperty(Collections.singletonList(listElement));
		TestBeanWithListPropertyGetter targetBean = new TestBeanWithListPropertyGetter();

		// prepare the copier
		BeanCopier<TestBeanWithStringListProperty, TestBeanWithListPropertyGetter> copier = new BeanCopier<>();
		copier.setUseTargetPropertiesAsSource(true);
		copier.setUseTargetCollectionGetterWithAddAll();
		copier.setUseSimpleCollectionConversion("listProperty");

		// copy
		copier.copy(sourceBean, targetBean);

		List<String> targetList = targetBean.getListProperty();
		assertThat(targetList, hasSize(1));
		assertEquals(listElement, targetList.get(0));
	}

	/**
     *
     */
	@Test
	public void testCopyListPropertyOfComplexType() {

		String name1 = "hallo";

		// prepare the objects
		TestBeanWithStringListProperty sourceBean = new TestBeanWithStringListProperty();
		sourceBean.setListProperty(Collections.singletonList(name1));
		TestBeanWithBeanListProperty targetBean = new TestBeanWithBeanListProperty();

		// prepare the copier
		BeanCopier<TestBeanWithStringListProperty, TestBeanWithBeanListProperty> copier = new BeanCopier<>();
		copier.setTargetPropertyConverter("listProperty", new AbstractCollectionConverter<String, DefaultTestBean>() {

			@Override
			protected DefaultTestBean convertElement(String name1) {
				DefaultTestBean bean = new DefaultTestBean();
				bean.setName1(name1);
				return bean;
			}
		});

		// copy
		copier.copy(sourceBean, targetBean);

		// test
		List<DefaultTestBean> targetList = targetBean.getListProperty();
		assertThat(targetList, hasSize(1));
		DefaultTestBean element = targetList.get(0);
		assertEquals(name1, element.getName1());
	}

	/**
     *
     */
	@Test
	public void testBuiltInPropertyConversion() {

		class BeanWithDate {
			private Date date;

			public Date getDate() {
				return this.date;
			}

			public void setDate(Date date) {
				this.date = date;
			}
		}

		class BeanWithCalendar {
			private Calendar date;

			public Calendar getDate() {
				return this.date;
			}

			public void setDate(Calendar date) {
				this.date = date;
			}
		}

		// prepare
		BeanWithDate sourceBean = new BeanWithDate();
		sourceBean.setDate(new Date());
		BeanWithCalendar targetBean = new BeanWithCalendar();
		targetBean.setDate(null);

		// copy
		BeanCopier<BeanWithDate, BeanWithCalendar> copier = new BeanCopier<>();
		copier.setEnableBuiltInConversion(true);
		copier.setEnablePrivilegedAccess(true);
		copier.copy(sourceBean, targetBean);

		// test
		assertNotNull(targetBean.getDate());
		assertEquals(sourceBean.getDate().getTime(), targetBean.getDate().getTimeInMillis());
	}

	/**
     *
     */
	@Test
	public void testBuiltInPropertyConversionForProperty() {

		class BeanWithDate {
			private Date date;

			public Date getDate() {
				return this.date;
			}

			public void setDate(Date date) {
				this.date = date;
			}
		}

		class BeanWithCalendar {
			private Calendar date;

			public Calendar getDate() {
				return this.date;
			}

			public void setDate(Calendar date) {
				this.date = date;
			}
		}

		// prepare
		BeanWithDate sourceBean = new BeanWithDate();
		sourceBean.setDate(new Date());
		BeanWithCalendar targetBean = new BeanWithCalendar();
		targetBean.setDate(null);

		// copy
		BeanCopier<BeanWithDate, BeanWithCalendar> copier = new BeanCopier<>();
		copier.setUseBuiltInConversion("date", true);
		copier.setEnablePrivilegedAccess(true);
		copier.copy(sourceBean, targetBean);

		// test
		assertNotNull(targetBean.getDate());
		assertEquals(sourceBean.getDate().getTime(), targetBean.getDate().getTimeInMillis());
	}

	/**
     *
     */
	@Test
	public void testIgnoreNullSourceValues() {

		// prepare
		DefaultTestBean sourceBean = new DefaultTestBean();
		sourceBean.setName1(null);
		DefaultTestBean targetBean = new DefaultTestBean();
		targetBean.setName2("name2");

		// copy
		BeanCopier<DefaultTestBean, DefaultTestBean> copier = new BeanCopier<>();
		copier.setIgnoreNullSourceValues(true);
		copier.copy(sourceBean, targetBean);

		// assert
		assertEquals("name2", targetBean.getName2());
	}

	/**
     *
     */
	@Test
	public void testAddDefaultConverter() {

		// prepare
		DefaultTestBean sourceBean = new DefaultTestBean();
		sourceBean.setName1("name1");
		sourceBean.setName2("name2");
		DefaultTestBean targetBean = new DefaultTestBean();

		// copy
		BeanCopier<DefaultTestBean, DefaultTestBean> copier = new BeanCopier<>();
		copier.addDefaultPropertyConverter(new AbstractSimplePropertyConverter<String, String>() {

			@Override
			protected String convert(String sourceValue) {
				return "name2".equals(sourceValue) ? "newName2" : sourceValue;
			}
		});
		copier.copy(sourceBean, targetBean);

		// assert
		assertEquals("name1", targetBean.getName1());
		assertEquals("newName2", targetBean.getName2());
	}
}
