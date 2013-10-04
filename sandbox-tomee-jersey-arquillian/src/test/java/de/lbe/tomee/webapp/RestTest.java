package de.lbe.tomee.webapp;

import java.net.URL;

import javax.ws.rs.core.MediaType;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

import de.lbe.tomee.webapp.ejb.HelloService;
import de.lbe.tomee.webapp.rest.HelloWorldResource;
import de.lbe.tomee.webapp.rest.JsonHello;
import de.lbe.tomee.webapp.rest.RestExceptionMapper;
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
		ShrinkWrapUtils.addPackagesWithoutTestClasses(archive, HelloService.class.getPackage().getName());
		return archive;
	}

	/**
	 * 
	 */
	@Test
	public void testHello() throws Exception {

		String url = this.contextPathURL + "rest/json";

		ClientConfig clientConfig = new DefaultClientConfig();
		clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
		Client client = Client.create(clientConfig);

		WebResource resource = client.resource(url);
		JsonHello hello = resource.accept(MediaType.APPLICATION_JSON_TYPE).get(JsonHello.class);

		assertNotNull(hello);
		assertEquals("Hello World!", hello.getMessage());
	}

	/**
	 * 
	 */
	@Test
	public void testValidation() throws Exception {

		String url = this.contextPathURL + "rest/post";

		ClientConfig clientConfig = new DefaultClientConfig();
		clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
		Client client = Client.create(clientConfig);

		JsonHello requestHello = new JsonHello();
		requestHello.setMessage("MSG");
		requestHello.setResponseId(System.currentTimeMillis());
		WebResource resource = client.resource(url);

		JsonHello responseHello =
			resource.accept(MediaType.APPLICATION_JSON_TYPE).type(MediaType.APPLICATION_JSON_TYPE).entity(requestHello).post(JsonHello.class);

		assertNotNull(responseHello);
		assertEquals(requestHello.getMessage(), responseHello.getMessage());
		assertNotNull(responseHello.getValidated());
		assertTrue(responseHello.getValidated());
	}

	/**
	 * 
	 */
	@Test
	public void testExceptionMapper() throws Exception {

		String url = this.contextPathURL + "rest/error";

		ClientConfig clientConfig = new DefaultClientConfig();
		clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
		Client client = Client.create(clientConfig);

		String message = "MESSAGE";
		WebResource resource = client.resource(url);
		resource = resource.path(message);
		ClientResponse response = resource.accept(MediaType.APPLICATION_JSON_TYPE).get(ClientResponse.class);

		assertEquals(444, response.getStatus());

		String responseEntity = response.getEntity(String.class);
		assertEquals(RestExceptionMapper.toErrorEntity(message, true), responseEntity);
	}
}
