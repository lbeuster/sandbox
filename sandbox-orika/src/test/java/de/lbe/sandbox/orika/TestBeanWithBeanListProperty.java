package de.lbe.sandbox.orika;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lars.beuster
 */
public class TestBeanWithBeanListProperty {

	private List<DefaultTestBean> beanListProperty = new ArrayList<>();

	public List<DefaultTestBean> getListProperty() {
		return this.beanListProperty;
	}

	public void setListProperty(List<DefaultTestBean> beanListProperty) {
		this.beanListProperty = beanListProperty;
	}
}
