package myflyway;

import java.sql.Connection;

import com.googlecode.flyway.core.api.migration.jdbc.JdbcMigration;

/**
 * @author lbeuster
 */
public class V2__TestMigration implements JdbcMigration {

	@Override
	public void migrate(Connection connection) throws Exception {
		System.out.println("MIGRATION");
	}
}
