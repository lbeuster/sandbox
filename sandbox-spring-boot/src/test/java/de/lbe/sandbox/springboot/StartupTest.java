package de.lbe.sandbox.springboot;

import org.junit.Test;
import org.slf4j.LoggerFactory;

import de.asideas.lib.commons.test.restclient.RestRequest;
import de.asideas.lib.commons.test.restclient.RestTarget;
import de.asideas.lib.commons.util.logging.mdc.RequestId;

/**
 * @author lbeuster
 */
public class StartupTest extends AbstractSpringBootTest {

	@Test
	public void testUserRealm() throws Exception {
		// Spring-Security not properly configured
		this.restClient.path("/protected").get().assertIsStatusOk();
	}

	@Test
	public void testDefaultURL() throws Exception {
		// Spring-Security not properly configured
		LoggerFactory.getLogger(getClass()).info("current requestID: {}", RequestId.getEffective());
		RestTarget<RestRequest> path = this.restClient.path("/test");
		path.get().assertIsStatusOk();
	}
}