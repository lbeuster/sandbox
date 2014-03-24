package de.lbe.sandbox.orika;

import ma.glasnost.orika.MappingException;

/**
 * @author lbeuster
 */
public class UnknownPropertyException extends MappingException {

	private static final long serialVersionUID = 1L;

	public UnknownPropertyException(String message) {
		super(message);
	}
}
