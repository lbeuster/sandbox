package de.lbe.sandbox.jboss7.ejb31.server;

import java.io.Serializable;

/**
 * 
 */
public class Language implements Serializable {

	private final int id;

	Language(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}
}
