package de.lbe.sandbox.tomcat;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.asideas.lib.commons.test.junit.AbstractJUnit4Test;
import de.lbe.sandbox.tomcat.testapp.TestBean;

/**
 * @author lars.beuster
 */
public class Jersey2Test extends AbstractJUnit4Test {

	protected Client restClient;

	protected TestTomcat tomcat;

	@Before
	public void setUp() {
		this.tomcat = TestTomcat.boot();
		// DefaultClientConfig clientConfig = new DefaultClientConfig();
		// clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
		this.restClient = ClientBuilder.newClient();

		Logger logger = LoggerFactory.getLogger(getClass());
		// LoggingFilter loggingFilter = new LoggingFilter(java.util.logging.Logger.getLogger(logger.getName()));
		// this.restClient.addFilter(loggingFilter);

	}

	@Test
	public void testGET() throws Exception {
		String s = restClient.target(tomcat.getWebappContextURL()).path("/rest").request().get(String.class);
		System.out.println(s);
	}

	@Test
	public void testPOST() throws Exception {
		TestBean bean = new TestBean();
		bean.setName("hallo");
		TestBean entity =
			restClient.target(tomcat.getWebappContextURL()).path("/rest").request().accept(MediaType.APPLICATION_JSON).post(Entity.entity(bean, MediaType.APPLICATION_JSON_TYPE))
				.readEntity(TestBean.class);
		System.out.println(entity);
		assertEquals(1, entity.getValidationCount());
	}
}
