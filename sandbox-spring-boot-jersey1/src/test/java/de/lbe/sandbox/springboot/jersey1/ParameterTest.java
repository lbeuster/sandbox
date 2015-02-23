package de.lbe.sandbox.springboot.jersey1;

import org.junit.Test;

/**
 * @author lbeuster
 */
public class ParameterTest extends AbstractSpringBootJersey1Test {

	@Test
	public void testIntegerParame() throws Exception {
		String message = target("/api/hello/intParam/789").get(String.class);
		assertEquals("789", message);
	}
}