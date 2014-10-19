package de.lbe.sandbox.springboot;

import org.junit.Test;

import de.asideas.lib.commons.lang.ClassLoaderUtils;
import de.asideas.lib.commons.test.junit.AbstractJUnit4Test;
import de.lbe.sandbox.springboot.old.OldSpringConfiguration;
import de.lbe.sandbox.springboot.old.SpringApplication;

/**
 * @author lbeuster
 */
public class MainTest extends AbstractJUnit4Test {

	@Test
	public void testMain() throws Exception {
		System.out.println(ClassLoaderUtils.toString(getClass().getClassLoader()));
		Main.main(new String[0]);
		System.out.println("ready");
		Thread.sleep(10_000);
	}

	@Test
	public void testOldTomcat() throws Exception {
		SpringApplication.main(OldSpringConfiguration.class);
		System.out.println("ready");
	}
}