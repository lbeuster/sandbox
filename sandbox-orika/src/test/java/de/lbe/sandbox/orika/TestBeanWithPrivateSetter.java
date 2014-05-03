package de.lbe.sandbox.orika;

/**
 * @author lars.beuster
 */
public class TestBeanWithPrivateSetter {

	private String internalProperty = null;

	@SuppressWarnings("unused")
	private void setProperty(String property) {
		this.internalProperty = property;
	}

	public String getProperty() {
		return this.internalProperty;
	}
}
