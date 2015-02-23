package de.lbe.sandbox.spring.retry;

import org.junit.After;
import org.junit.Before;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.test.context.ContextConfiguration;

import de.asideas.ipool.commons.lib.spring.test.AbstractSpringIT;

/**
 * @author lbeuster
 */
@ContextConfiguration(classes = AbstractSpringRetryTest.TestConfiguration.class)
public abstract class AbstractSpringRetryTest extends AbstractSpringIT {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() {
	}

	/**
	 *
	 */
	@Configuration
	@Import({ TestService.class })
	@EnableRetry(proxyTargetClass = true)
	public static class TestConfiguration {
		// no impl - just the annotations
	}
}