package de.lbe.sandbox.orika;

import java.util.ArrayList;
import java.util.List;

/**
 * This bean is like the generated jaxb-stubs. We don't have a setter on the list-property. Insteat the internal list is exposed via the getter and we
 * have to use the list operations to add elements.
 * 
 * @author lars.beuster
 */
public class TestBeanWithListPropertyGetter {

	private final List<String> listProperty = new ArrayList<>();

	public List<String> getListProperty() {
		return this.listProperty;
	}
}
