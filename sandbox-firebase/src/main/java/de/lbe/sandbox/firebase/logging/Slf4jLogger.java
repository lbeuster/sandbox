package de.lbe.sandbox.firebase.logging;

import org.slf4j.LoggerFactory;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.logging.Logger;

/**
 * @author lbeuster
 */
public class Slf4jLogger implements Logger {

	private final Level logLevel;

	/**
	 *
	 */
	public Slf4jLogger() {
		this.logLevel = adjustLogLevel(LoggerFactory.getLogger(FirebaseApp.class.getPackage().getName()));
	}

	/**
	 *
	 */
	@Override
	public void onLogMessage(Level level, String loggerName, String message, long timestamp) {
		org.slf4j.Logger logger = LoggerFactory.getLogger(loggerName);
		switch (level) {
			case NONE:
				break;
			case ERROR:
				logger.error(message);
				break;
			case WARN:
				logger.warn(message);
				break;
			case INFO:
				logger.info(message);
				break;
			case DEBUG:
				logger.debug(message);
				break;
		}
	}

	@Override
	public Level getLogLevel() {
		return this.logLevel;
	}

	private static Level adjustLogLevel(org.slf4j.Logger logger) {
		if (logger.isDebugEnabled()) {
			return Level.DEBUG;
		}
		if (logger.isInfoEnabled()) {
			return Level.INFO;
		}
		if (logger.isWarnEnabled()) {
			return Level.WARN;
		}
		if (logger.isErrorEnabled()) {
			return Level.ERROR;
		}
		return Level.NONE;
	}
}
