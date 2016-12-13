package de.lbe.sandbox.spring.security;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import de.asideas.ipool.commons.lib.spring.boot.ExtendedSpringApplication;

/**
 * @author lbeuster
 */
@SpringBootApplication
public class Main {

	public static void main(String... args) {
		new ExtendedSpringApplication(Main.class).keepMainThreadAlive(true).run();
	}
}
