package de.lbe.sandbox.springboot;

import org.junit.Test;

/**
 * @author lbeuster
 */
public class ServerTest {

	@Test
	public void startServer() throws Exception {
		ServerMain.main();
		System.out.println("started server");
	}
}