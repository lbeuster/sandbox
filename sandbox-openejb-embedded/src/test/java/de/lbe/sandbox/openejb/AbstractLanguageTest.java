package de.lbe.sandbox.openejb;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Rule;

import de.asideas.lib.commons.dbunit.DBUnit;
import de.asideas.lib.commons.dbunit.DataSourceDBUnit;
import de.asideas.lib.commons.test.junit.AbstractJUnit4Test;

/**
 *
 */
public abstract class AbstractLanguageTest extends AbstractJUnit4Test {

	@Resource(name = "LanguageTX")
	private DataSource dataSource;

	private DBUnit dbUnit;

	@Rule
	public OpenEJBRule openEJBRule = new OpenEJBRule();

	/**
	 * 
	 */
	@Rule
	public DBUnit getDBUnit() throws Exception {

		if (this.dbUnit == null) {

			// ensure that we get the DS injected before we use it
			this.openEJBRule.bind(this);

			this.dbUnit = new DataSourceDBUnit(this.dataSource);

			this.dbUnit.addDataSetFiles("sql/language/data.xml");
			this.dbUnit.addSetUpSqlFiles("sql/language/create-tables.sql");
			this.dbUnit.addTearDownSqlFiles("sql/language/drop-tables.sql");
		}
		return this.dbUnit;
	}

	/**
	 * 
	 */
	@Before
	public void setUp() throws Exception {
		OpenEJBContainer.start();

		// needed for @EJB, @Resource
		OpenEJBContainer.getContext().bind("inject", this);
	}
}
