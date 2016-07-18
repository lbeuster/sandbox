package de.lbe.sandbox.splunk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import ch.qos.logback.classic.LoggerContext;

/**
 * @author lars.beuster
 */
public class Main {

	private static final Logger LOGGER = LoggerFactory.getLogger("larstest");

	public static void main(String[] args) throws Exception {

		// wait a few seconds because the TCP connection is established async
		Thread.sleep(2000);

		MDC.put("larsmdc", "mdcValue");

		for (int i = 0; i < 21; i++) {
			LOGGER.info("LOGGER INFO1");
		}
		LOGGER.error("LOGGER INFO2");
		LOGGER.error("Info stacktrace", new Exception("TEST", new Exception("TEST1")));

		Thread.sleep(2000);

		LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
		loggerContext.stop();
	}
}
