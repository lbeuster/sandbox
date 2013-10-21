package de.lbe.sandbox.tomee;

import java.io.File;

import org.apache.tomee.embedded.Configuration;
import org.apache.tomee.embedded.Container;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;

import de.asideas.lib.commons.shrinkwrap.ShrinkWrapUtils;
import de.asideas.lib.commons.test.junit.AbstractJUnit4Test;
import de.lbe.sandbox.tomee.testapp.TestServlet;

/**
 * @author lars.beuster
 */
public class EmbeddedTomeeTest extends AbstractJUnit4Test {

	@Test
	public void testStart() throws Exception {
		Container container = new Container();
		Configuration configuration = new Configuration();
		configuration.setHttpPort(7070);
		container.setup(configuration);
		container.start();

		File war = deployment();
		container.deploy(null, war, false);

		// put your breakpoint here
		container.stop();
	}

	private File deployment() {
		WebArchive war = ShrinkWrap.create(WebArchive.class);
		// ShrinkWrapUtils.addDirectory(war, "src/test/webapp");
		war.addClass(TestServlet.class);
		File file = new File("target/test.war");
		ShrinkWrapUtils.exportToFile(war, "target/test.war", true);
		return file;
	}
}
