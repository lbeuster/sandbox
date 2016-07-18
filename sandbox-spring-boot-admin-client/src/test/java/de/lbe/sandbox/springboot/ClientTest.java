package de.lbe.sandbox.springboot;

import org.junit.Test;

/**
 * @author lbeuster
 */
public class ClientTest {

	@Test
	public void startClient() throws Exception {
		ClientMain.main();
		System.out.println("started client");
	}
}