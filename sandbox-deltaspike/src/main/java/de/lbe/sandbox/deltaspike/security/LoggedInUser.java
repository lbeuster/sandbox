package de.lbe.sandbox.deltaspike.security;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class LoggedInUser {

	private boolean valid = false;

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}
}