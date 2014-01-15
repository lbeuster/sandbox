package de.lbe.sandbox.flyway;

import org.junit.Test;

import com.googlecode.flyway.core.Flyway;

import de.asideas.lib.commons.test.junit.AbstractJUnit4Test;

/**
 * @author lbeuster
 */
public class FlywayTest extends AbstractJUnit4Test {

	public static final String DATABASE_URL = "jdbc:mysql://localhost:3306/flyway?useUnicode=true&characterEncoding=utf8";

	@Test
	public void testFlyway() {
		Flyway flyway = new Flyway();
		flyway.setLocations("/myflyway");
		flyway.setDataSource(DATABASE_URL, "root", "root");
		flyway.setValidateOnMigrate(true);

		// just deletes metadata about a failed migration
		flyway.repair();
		flyway.migrate();
	}
}
