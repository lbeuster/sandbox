package de.lbe.sandbox.tomcat.testapp;

/**
 * @author lars.beuster
 */
@ValidTestBean
public class TestBean {

	private String name;

	private int validationCount = 0;

	private boolean cdiActive = false;
	
	private boolean contextInjectionActive = false;

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

	public boolean isCdiActive() {
		return cdiActive;
	}

	public void setCdiActive(boolean cdiActive) {
		this.cdiActive = cdiActive;
	}

	public boolean isContextInjectionActive() {
		return contextInjectionActive;
	}

	public void setContextInjectionActive(boolean contextInjectionActive) {
		this.contextInjectionActive = contextInjectionActive;
	}
}