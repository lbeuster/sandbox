package de.lbe.sandbox.orika;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lars.beuster
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
