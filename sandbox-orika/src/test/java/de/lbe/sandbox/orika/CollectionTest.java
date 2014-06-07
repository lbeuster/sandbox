package de.lbe.sandbox.orika;

import static org.hamcrest.Matchers.hasSize;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

/**
 * @author lbeuster
 */
public class CollectionTest extends AbstractOrikaTest {

	/**
     *
     */
	@Test
	public void testCopyListPropertyOfSimpleType() {

		String listElement = "hallo";

		// prepare the objects
		TestBeanWithStringListProperty source = new TestBeanWithStringListProperty();
		source.setListProperty(Collections.singletonList(listElement));
		TestBeanWithStringListProperty target = new TestBeanWithStringListProperty();

		// copy
		strictMapperFactory().getMapperFacade(TestBeanWithStringListProperty.class, TestBeanWithStringListProperty.class, false).map(source, target);

		// assert
		List<String> targetList = target.getListProperty();
		assertNotSame(source.getListProperty(), target.getListProperty());
		assertThat(targetList, hasSize(1));
		assertEquals(listElement, targetList.get(0));
	}

	/**
	 * 
	 */
	public class TestBeanWithStringListProperty {

		private List<String> listProperty = new ArrayList<>();

		public List<String> getListProperty() {
			return this.listProperty;
		}

		public void setListProperty(List<String> listProperty) {
			this.listProperty = listProperty;
		}
	}
}
