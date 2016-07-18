package de.lbe.sandbox.realtime;

import org.junit.Before;
import org.junit.Rule;

import de.asideas.ipool.commons.lib.test.junit.AbstractJUnitTest;
import de.asideas.ipool.commons.lib.test.junit.rules.TestFilesRule;

/**
 * @author lbeuster
 */
public abstract class RealtimeTest extends AbstractJUnitTest {

	@Rule
	public final TestFilesRule testFiles = new TestFilesRule();

	/**
	 *
	 */
	@Before
	public void setUp() throws Exception {
	}

}
