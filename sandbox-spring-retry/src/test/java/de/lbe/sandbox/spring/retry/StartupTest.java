package de.lbe.sandbox.spring.retry;

import javax.inject.Inject;

import org.junit.Test;

/**
 * @author lbeuster
 */
public class StartupTest extends AbstractSpringRetryTest {

	@Inject
	private TestService service;

	@Test
	public void testRetry() throws Exception {
		service.service();
	}
}