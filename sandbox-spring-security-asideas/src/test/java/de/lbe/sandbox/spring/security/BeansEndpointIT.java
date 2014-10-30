package de.lbe.sandbox.spring.security;

import org.junit.Test;

import de.asideas.lib.commons.test.restclient.RestTarget;

/**
 * @author lbeuster
 */
public class BeansEndpointIT extends AbstractTest {

	@Test
	public void testBeans() throws Exception {
		RestTarget<?> target = addBasicAuth(this.restClient.path("/beans"));
		target.get().assertIsStatusOk();
	}
}
