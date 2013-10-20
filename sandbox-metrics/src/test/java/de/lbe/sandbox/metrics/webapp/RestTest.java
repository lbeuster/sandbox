package de.lbe.sandbox.metrics.webapp;

import java.net.URL;

import javax.ws.rs.core.MediaType;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

import de.lbe.sandbox.metrics.webapp.Hello;
import de.lbe.sandbox.metrics.webapp.HelloWorldResource;
import de.asideas.lib.commons.arquillian.AbstractJUnit4ArquillianTest;
import de.asideas.lib.commons.shrinkwrap.ShrinkWrapUtils;

/**
 * @author lars.beuster
 */
public class RestTest extends AbstractJUnit4ArquillianTest {

	@ArquillianResource
	private URL contextPathURL;

	/**
	 * 
	 */
	@Deployment(testable = false)
	public static WebArchive deployment() {
		WebArchive archive = ShrinkWrapUtils.prepareCdiWar("sandbox.war");
		ShrinkWrapUtils.addDirectory(archive, "src/main/webapp");
		ShrinkWrapUtils.addPackagesWithoutTestClasses(archive, HelloWorldResource.class.getPackage().getName());
		return archive;
	}

	/**
	 * 
	 */
	@Test
	public void testHello() throws Exception {

		String url = this.contextPathURL + "rest/hello";

		ClientConfig clientConfig = new DefaultClientConfig();
		clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
		Client client = Client.create(clientConfig);

		WebResource resource = client.resource(url);
		Hello hello = resource.accept(MediaType.APPLICATION_JSON_TYPE).get(Hello.class);

		assertNotNull(hello);
		assertEquals("HALLO", hello.getMessage());
	}
}
