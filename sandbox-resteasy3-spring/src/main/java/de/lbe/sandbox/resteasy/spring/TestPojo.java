package de.lbe.sandbox.resteasy.spring;

import javax.validation.constraints.NotNull;

/**
 * @author lbeuster
 */
public class TestPojo {

	@NotNull
	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}