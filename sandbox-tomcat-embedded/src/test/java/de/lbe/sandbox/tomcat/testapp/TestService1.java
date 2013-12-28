package de.lbe.sandbox.tomcat.testapp;

import javax.validation.Valid;

/**
 * @author lars.beuster
 */
public class TestService1 {

	public TestBean service(@Valid TestBean testBean) {
		return testBean;
	}
}
