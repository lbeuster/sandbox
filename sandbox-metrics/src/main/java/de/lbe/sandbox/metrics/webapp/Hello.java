package de.lbe.sandbox.metrics.webapp;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 */
@XmlRootElement
public class Hello {

	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
