package de.lbe.sandbox.springboot;

import org.junit.Test;

import de.asideas.ipool.commons.lib.test.junit.AbstractJUnitTest;

/**
 * @author lbeuster
 */
public class ServerTest extends AbstractJUnitTest {

	@Test
	public void testStartup() throws Exception {
		ServerMain.main();
		System.out.println("started server");
	}
}