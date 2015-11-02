package de.lbe.sandbox.splunk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.asideas.ipool.commons.lib.util.logging.logback.LogbackUtils;

/**
 * @author lars.beuster
 */
public class Main {

	private static final Logger LOGGER = LoggerFactory.getLogger("larstest");

	public static void main(String[] args) throws Exception {

		// wait a few seconds because the TCP connection is established async
		Thread.sleep(2000);

		for (int i = 0; i < 20; i++) {
			LOGGER.info("LOGGER INFO1");
		}
		LOGGER.error("LOGGER INFO2");

		Thread.sleep(2000);

		LogbackUtils.shutdown();
	}
}
