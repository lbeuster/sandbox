package de.lbe.sandbox.splunk;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.asideas.lib.commons.test.junit.AbstractJUnit4Test;
import de.asideas.lib.commons.util.logging.logback.LogbackUtils;

/**
 * @author lars.beuster
 */
public class LoggingTest extends AbstractJUnit4Test {

	private static final Logger LOGGER = LoggerFactory.getLogger(LoggingTest.class);

	@Test
	public void start() throws Exception {

		Exception cause = rootCause();

		// wait a few seconds because the TCP connection is established async
		Thread.sleep(2000);

		// new Main().start(false);
		LOGGER.info("LOGGER INFO1");
		LOGGER.info("LOGGER INFO2");
		LOGGER.error("LOGGER ERROR", new Exception("Exception message", cause));
		LOGGER.error("LOGGER ERROR", new NullPointerException("Exception message"));
		LOGGER.error("LOGGER ERROR", new IllegalArgumentException("Exception message", cause));
		LOGGER.error("LOGGER ERROR", new OutOfMemoryError("Exception message"));
		LogbackUtils.shutdown();
	}

	private Exception rootCause() {
		return new RuntimeException("runtimeEx");
	}
}
