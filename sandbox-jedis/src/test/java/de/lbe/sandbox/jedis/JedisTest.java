package de.lbe.sandbox.jedis;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.awaitility.Duration.TWO_SECONDS;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import de.asideas.ipool.commons.lib.spring.boot.test.AbstractSpringBootIT;
import de.lbe.sandbox.jedis.JedisTest.TestConfiguration;

/**
 * @author lbeuster
 */
@SpringBootTest(classes = TestConfiguration.class)
public class JedisTest extends AbstractSpringBootIT {

	@Inject
	private StringRedisTemplate jedis;

	private ValueOperations<String, String> valueOps;

	@Before
	public void setUp() {
		this.valueOps = jedis.opsForValue();
	}

	/**
	 *
	 */
	@Test
	public void testGetAndSet() {
		String key = getTestMethodName();
		String value = valueOps.get(key);
		assertNull(value);

		valueOps.set(key, "value");

		value = valueOps.get(key);
		assertEquals("value", value);
	}

	/**
	 *
	 */
	@Test
	public void testExpiration() {
		String key = getTestMethodName();
		final String value = "VALUE";
		String actualValue = valueOps.get(key);
		assertNull(actualValue);

		valueOps.set(key, value, 1, SECONDS);
		actualValue = valueOps.get(key);
		assertEquals(value, actualValue);

		await().atMost(TWO_SECONDS).until(() -> {
			String localActualValue = valueOps.get(key);
			assertNull("value", localActualValue);
		});
	}

	/**
	 *
	 */
	@EnableAutoConfiguration
	@SpringBootConfiguration
	public static class TestConfiguration {
		// only the annotations
	}
}
