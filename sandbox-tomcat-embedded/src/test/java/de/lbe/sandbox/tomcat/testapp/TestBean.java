package de.lbe.sandbox.tomcat.testapp;

/**
 * @author lars.beuster
 */
@ValidTestBean
public class TestBean {

	private String name;

	private int validationCount = 0;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void incValidationCount() {
		this.validationCount++;
	}

	public int getValidationCount() {
		return this.validationCount;
	}
}