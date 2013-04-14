package de.lbe.sandbox.openejb;

import javax.annotation.Resource;
import javax.sql.DataSource;

import com.zanox.lib.commons.dbunit.AbstractJUnit4DBUnitTest;
import com.zanox.lib.commons.dbunit.DBUnitExtensions;
import com.zanox.lib.commons.dbunit.DataSourceDBUnitExtension;

/**
 *
 */
public abstract class AbstractLanguageTest extends AbstractJUnit4DBUnitTest {

	@Resource(name = "LanguageTX")
	private DataSource dataSource;

	/**
	 * 
	 */
	@Override
	protected void setUpDBUnit() throws Exception {
		OpenEJBContainer.start();

		// needed for @EJB, @Resource
		OpenEJBContainer.getContext().bind("inject", this);

		addDataSetFiles("sql/language/data.xml");
		addSetUpSqlFiles("sql/language/create-tables.sql");
		addTearDownSqlFiles("sql/language/drop-tables.sql");
	}

	/**
     *
     */
	@Override
	protected DBUnitExtensions createDBUnitExtensions() {
		return new DBUnitExtensions(new DataSourceDBUnitExtension(this.dataSource));
	}
}
