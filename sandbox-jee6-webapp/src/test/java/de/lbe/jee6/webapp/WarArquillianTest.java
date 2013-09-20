package de.lbe.jee6.webapp;

import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;

import de.asideas.lib.commons.arquillian.AbstractJUnit4ArquillianTest;
import de.asideas.lib.commons.shrinkwrap.ShrinkWrapUtils;
import de.lbe.jee6.webapp.rest.HelloWorldConfig;

/**
 * @author lars.beuster
 */
public class WarArquillianTest extends AbstractJUnit4ArquillianTest {

	@ArquillianResource
	private URL contextPathURL;

	/**
	 * 
	 */
	@Deployment(testable = false)
	public static WebArchive deployment() {
		WebArchive archive = ShrinkWrap.create(WebArchive.class, "test-webapp.war").addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
		ShrinkWrapUtils.addArchiveOfClass(archive, HelloWorldConfig.class);
		ShrinkWrapUtils.addDirectory(archive, "src/main/webapp");
		ShrinkWrapUtils.exportToFile(archive, "/tmp/sandbox.war", true);
		return archive;
	}

	/**
	 * In contrast to JBoss we can use the complete test-classpath we we are inside the container. With JBoss we only have the module-classloader
	 * without the test-classpath.
	 * 
	 * TomEE doesn't deploy Jars as BeanArchives (by default?).
	 */
	@Test
	public void testStartup() throws Exception {
		// System.out.println(this.contextPathURL);

	}
}
