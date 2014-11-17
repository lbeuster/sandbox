package de.lbe.sandbox.kafka.embedded;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import kafka.server.KafkaConfig;
import kafka.server.KafkaServer;

import de.asideas.lib.commons.lang.MoreStrings;
import de.asideas.lib.commons.net.Sockets;
import de.asideas.lib.commons.nio.MoreFiles;

/**
 * @author lbeuster
 */
public class OldEmbeddedKafka {

	private final String zkConnection;

	private final String hostname = "localhost";

	private int port;

	private final Properties properties;

	private String brokerId;

	private Path logDir;

	private KafkaServer broker;

	/**
	 *
	 */
	public OldEmbeddedKafka(String zkConnection, Properties properties, int port) {
		this.zkConnection = zkConnection;
		this.port = port;
		this.properties = properties;
	}

	public OldEmbeddedKafka(String zkConnection, Properties baseProperties) {
		this(zkConnection, baseProperties, -1);
	}

	public OldEmbeddedKafka(String zkConnection, int port) {
		this(zkConnection, new Properties(), port);
	}

	public OldEmbeddedKafka(String zkConnection) {
		this(zkConnection, new Properties(), -1);
	}

	/**
	 *
	 */
	public void start() throws IOException {

		// adjust values
		if (this.port < 0) {
			this.port = Sockets.findAvailableTCPPort();
		}
		if (this.brokerId == null) {
			this.brokerId = String.valueOf(this.port);
		}
		this.logDir = Files.createTempDirectory("kafka-local-");

		// prepare properties
		Properties properties = new Properties(this.properties);
		properties.setProperty("zookeeper.connect", this.zkConnection);
		properties.setProperty("broker.id", this.brokerId);
		properties.setProperty("host.name", this.hostname);
		properties.setProperty("port", Integer.toString(port));
		properties.setProperty("log.dir", logDir.toFile().getAbsolutePath());
		properties.setProperty("log.flush.interval.messages", String.valueOf(1));

		// start kafka
		this.broker = new KafkaServer(new KafkaConfig(properties), null);
		this.broker.startup();
	}

	/**
	 *
	 */
	public void stop() {
		try {
			this.broker.shutdown();
		} finally {
			MoreFiles.deleteQuietly(this.logDir);
		}
	}

	public String getConnection() {
		return this.hostname + ":" + this.port;
	}

	@Override
	public String toString() {
		return MoreStrings.format("EmbeddedKafka[%s]", getConnection());
	}
}
