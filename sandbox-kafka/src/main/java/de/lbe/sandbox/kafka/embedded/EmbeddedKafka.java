package de.lbe.sandbox.kafka.embedded;

import java.util.Properties;

import kafka.server.KafkaConfig;
import kafka.server.KafkaServer;
import kafka.utils.MockTime;
import kafka.utils.TestUtils;
import kafka.utils.Time;

import de.asideas.lib.commons.lang.MoreStrings;

/**
 * @author lbeuster
 */
public class EmbeddedKafka {

	private final String zkConnection;

	private final String hostname = "localhost";

	private int port;

	private int brokerId;

	private KafkaServer server;

	/**
	 *
	 */
	public EmbeddedKafka(String zkConnection, int port) {
		this.zkConnection = zkConnection;
		this.port = port;
	}

	public EmbeddedKafka(String zkConnection) {
		this(zkConnection, -1);
	}

	/**
	 *
	 */
	public void start() {

		// adjust port
		if (this.port < 0) {
			this.port = TestUtils.choosePort();
		}
		if (this.brokerId < 0) {
			this.brokerId = this.port;
		}

		// prepare properties
		Properties props = TestUtils.createBrokerConfig(this.brokerId, this.port);
		props.put("zookeeper.connect", this.zkConnection);
		KafkaConfig config = new KafkaConfig(props);

		// start server
		Time time = new MockTime();
		this.server = TestUtils.createServer(config, time);
	}

	/**
	 *
	 */
	public void shutdown() {
		if (this.server != null) {
			this.server.shutdown();
		}
	}

	public String getConnection() {
		return this.hostname + ":" + this.port;
	}

	public int getPort() {
		return this.port;
	}

	public KafkaServer getServer() {
		return this.server;
	}

	@Override
	public String toString() {
		return MoreStrings.format("EmbeddedKafka[%s]", getConnection());
	}
}
