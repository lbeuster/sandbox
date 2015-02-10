package de.lbe.sandbox.springboot.jersey1;

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
