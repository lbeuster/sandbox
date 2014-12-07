package de.lbe.sandbox.springboot.jersey2;

import org.springframework.stereotype.Component;

/**
 * @author lbeuster
 */
@Component
public class HelloService {

	public HelloService() {
		System.out.println("HelloService.<init>");
	}
}
