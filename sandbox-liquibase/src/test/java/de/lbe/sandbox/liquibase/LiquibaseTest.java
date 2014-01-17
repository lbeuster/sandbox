package de.lbe.sandbox.liquibase;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.sql.DataSource;

import liquibase.integration.cdi.CDILiquibaseConfig;
import liquibase.integration.cdi.annotations.LiquibaseType;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.resource.ResourceAccessor;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import de.asideas.lib.commons.arquillian.AbstractJUnit4ArquillianTest;
import de.asideas.lib.commons.shrinkwrap.ShrinkWrapUtils;

/**
 * - Umständlich zu benutzen - automatisch beim Hochfahren von CDI (kann man natürlich auch explizit aufrufen) - kann keinen MySQL Dump ausführen, nicht mal die create-Skipte der
 * Community.
 * 
 * @author lbeuster
 */
public class LiquibaseTest extends AbstractJUnit4ArquillianTest {

	public static final String DATABASE_URL = "jdbc:mysql://localhost:3306/liquibase?useUnicode=true&characterEncoding=utf8";

	@Deployment
	public static JavaArchive deployment() {
		JavaArchive archive = ShrinkWrapUtils.prepareCdiJar();
		return archive;
	}

	@Test
	public void testLiquibase() {
	}

	public static class LiquibaseProducer {

		@Produces
		@LiquibaseType
		public CDILiquibaseConfig createConfig() {
			CDILiquibaseConfig config = new CDILiquibaseConfig();
			config.setChangeLog("myliquibase/community-model.sql");
			return config;
		}

		@Produces
		@LiquibaseType
		@ApplicationScoped
		public DataSource createDataSource() {
			MysqlDataSource dataSource = new MysqlDataSource();
			dataSource.setUrl(DATABASE_URL);
			dataSource.setUser("root");
			dataSource.setPassword("root");
			return dataSource;
		}

		@Produces
		@LiquibaseType
		public ResourceAccessor create() {
			return new ClassLoaderResourceAccessor(getClass().getClassLoader());
		}

	}
}
