package de.lbe.sandbox.springboot;

import org.junit.Test;

import de.asideas.ipool.commons.lib.test.junit.AbstractJUnitTest;

/**
 * @author lbeuster
 */
public class ClientTest extends AbstractJUnitTest {

	@Test
	public void testStartup() throws Exception {
		ClientMain.main();
		System.out.println("started client");
	}
}