package de.lbe.sandbox.tomcat;

import java.io.File;

import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.naming.resources.VirtualDirContext;
import org.junit.Test;

import de.asideas.lib.commons.test.junit.AbstractJUnit4Test;

/**
 * @author lars.beuster
 */
public class EmbeddedTomcatTest extends AbstractJUnit4Test {

	@Test
	public void testStart() throws Exception {

		String webappDirLocation = "src/main/webapp/";
		Tomcat tomcat = new Tomcat();
		tomcat.setPort(9000);

		StandardContext ctx = (StandardContext) tomcat.addWebapp("/embeddedTomcat", new File(webappDirLocation).getAbsolutePath());

		// declare an alternate location for your "WEB-INF/classes" dir:
		File additionWebInfClasses = new File("target/test-classes");
		VirtualDirContext resources = new VirtualDirContext();
		resources.setExtraResourcePaths("/WEB-INF/classes=" + additionWebInfClasses);
		ctx.setResources(resources);
		//
		//
		// Tomcat tomcat = new Tomcat();
		// tomcat.setPort(9000);
		// tomcat.setBaseDir(".");
		// tomcat.enableNaming();

		// Context ctx = tomcat.addWebapp("/", new File(".").getAbsolutePath());
		// ((StandardJarScanner) ctx.getJarScanner()).setScanAllFiles(true);

		// FilterDef def = new FilterDef();
		// def.setFilterClass(Listener.class.getName());
		// ctx.addFilterDef(def);

		tomcat.start();
		// tomcat.getServer().await();
		//
		// EmbeddedTestTomcat tomcat = new EmbeddedTestTomcat();
		//
		// tomcat.start();
	}

	// private File deployment() {
	// WebArchive war = ShrinkWrap.create(WebArchive.class);
	// // ShrinkWrapUtils.addDirectory(war, "src/test/webapp");
	// war.addClass(TestServlet.class);
	// File file = new File("target/test.war");
	// ShrinkWrapUtils.exportToFile(war, "target/test.war", true);
	// return file;
	// }
}
