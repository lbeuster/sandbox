package de.lbe.sandbox.spring.security.rest;

public class TokenTransfer {

	private String token;

	public TokenTransfer(String token) {
		this.token = token;
	}

	public TokenTransfer() {
	}

	public String getToken() {
		return this.token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}