package de.lbe.sandbox.orika;

/**
 * We need a target bean with one additional property.
 * 
 * @author lars.beuster
 */
public class TestBeanWithAdditionalProperty {

	private String targetProperty;

	public String getTargetProperty() {
		return this.targetProperty;
	}

	public void setTargetProperty(String targetProperty) {
		this.targetProperty = targetProperty;
	}
}
