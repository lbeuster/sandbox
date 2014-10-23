package de.lbe.sandbox.springboot;

import org.junit.Test;

import de.asideas.lib.commons.test.junit.AbstractJUnit4Test;
import de.lbe.sandbox.springboot.old.OldSpringConfiguration;
import de.lbe.sandbox.springboot.old.SpringApplication;

/**
 * @author lbeuster
 */
public class OldEmbeddedTomcatTest extends AbstractJUnit4Test {

	/**
	 * 
	 */
	@Test
	public void testOldTomcat() throws Exception {
		SpringApplication app = SpringApplication.main(OldSpringConfiguration.class);
		app.close();
	}
}