package de.lbe.sandbox.tomcat;

import javax.ws.rs.core.MediaType;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.LoggingFilter;
import com.sun.jersey.api.json.JSONConfiguration;

import de.lbe.sandbox.tomcat.testapp.TestBean;
import de.asideas.lib.commons.test.junit.AbstractJUnit4Test;

/**
 * @author lars.beuster
 */
public class Jersey1Test extends AbstractJUnit4Test {

	protected Client restClient;

	protected TestTomcat tomcat;

	protected WebResource prepareClient() {
		return this.restClient.resource(tomcat.getWebappContextURL());
	}

	@Before
	public void setUp() {
		this.tomcat = TestTomcat.boot();
		DefaultClientConfig clientConfig = new DefaultClientConfig();
		clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
		this.restClient = Client.create(clientConfig);

		Logger logger = LoggerFactory.getLogger(getClass());
		LoggingFilter loggingFilter = new LoggingFilter(java.util.logging.Logger.getLogger(logger.getName()));
		this.restClient.addFilter(loggingFilter);

	}

	@Test
	public void testGET() throws Exception {
		String s = restClient.resource(tomcat.getWebappContextURL()).path("/rest").get(String.class);
		System.out.println(s);
	}

	/**
	 * 
	 */
	@Test
	public void testInjection() throws Exception {
		TestBean bean = prepareClient().path("rest/testInjection").get(TestBean.class);
		assertTrue(bean.isCdiActive());
	}

	/**
	 * 
	 */
	@Test
	public void testContextInjectionInCdiBean() throws Exception {
		TestBean bean = prepareClient().path("rest/testContextInjectionInCdiBean").get(TestBean.class);
		assertTrue(bean.isContextInjectionActive());
	}

	/**
	 * 
	 */
	@Test
	public void testContextInjection() throws Exception {
		TestBean bean = prepareClient().path("rest/testContextInjection").get(TestBean.class);
		assertTrue(bean.isContextInjectionActive());
	}

	@Test
	public void testPOST() throws Exception {
		TestBean bean = new TestBean();
		bean.setName("hallo");
		bean =
			restClient.resource(tomcat.getWebappContextURL()).path("/rest").accept(MediaType.APPLICATION_JSON).entity(bean).type(MediaType.APPLICATION_JSON).post(TestBean.class);
		assertEquals(1, bean.getValidationCount());
	}
}
