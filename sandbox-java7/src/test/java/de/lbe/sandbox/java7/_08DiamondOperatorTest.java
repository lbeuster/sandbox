package de.lbe.sandbox.java7;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

/**
 * Bei Konstruktoren von Klassen mit generischen Typen koennen die generischen Argument jetzt weggelassen werden, wenn
 * der Compiler diese "erraten" kann.
 */
public class _08DiamondOperatorTest extends Assert {

	/**
     * 
     */
	@Test
	public void testDiamondOperator() {
		// @NEW Wir koennen die generischen Typen weglassen
		Map<String, List<String>> map = new HashMap<>();
		assertNotNull(map);
	}
}
