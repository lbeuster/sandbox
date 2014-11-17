package de.lbe.sandbox.kafka.embedded;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import com.google.common.base.Verify;

/**
 * @author lbeuster
 */
public class OldEmbeddedKafkaCluster {

	private EmbeddedZookeeper zookeeper;

	private final List<Integer> ports;

	private final List<OldEmbeddedKafka> nodes;

	/**
	 *
	 */
	public OldEmbeddedKafkaCluster(List<Integer> ports) {
		Verify.verifyNotNull(ports, "ports");
		Verify.verify(ports.size() > 0, "ports.size must be > 0");

		this.ports = ports;
		this.nodes = new ArrayList<>();
		this.zookeeper = new EmbeddedZookeeper();
	}

	/**
	 *
	 */
	@SuppressWarnings("boxing")
	public OldEmbeddedKafkaCluster(int numberOfNodes) {
		this(Collections.nCopies(numberOfNodes, -1));
	}

	/**
	 *
	 */
	@SuppressWarnings("boxing")
	public OldEmbeddedKafkaCluster() {
		this(Collections.singletonList(-1));
	}

	/**
	 *
	 */
	@SuppressWarnings("boxing")
	public void start() throws IOException {

		this.zookeeper.start();

		for (int i = 0; i < ports.size(); i++) {
			Integer port = ports.get(i);
			if (port == null) {
				port = -1;
			}
			OldEmbeddedKafka node = new OldEmbeddedKafka(this.zookeeper.getConnectionURL(), port);
			node.start();
			this.nodes.add(node);
		}
	}

	/**
	 *
	 */
	public void stop() {
		try {
			for (OldEmbeddedKafka node : this.nodes) {
				node.stop();
			}
		} finally {
			this.zookeeper.shutdown();
		}
	}

	/**
	 *
	 */
	private String constructBrokerList() {
		StringBuilder sb = new StringBuilder();
		for (OldEmbeddedKafka node : this.nodes) {
			if (sb.length() > 0) {
				sb.append(",");
			}
			sb.append(node.getConnection());
		}
		return sb.toString();
	}

	/**
	 *
	 */
	public Properties getProducerProperties() {
		Properties properties = new Properties();
		properties.put("metadata.broker.list", getBrokerList());
		return properties;
	}

	/**
	 *
	 */
	public Properties getConsumerProperties() {
		Properties properties = new Properties();
		properties.put("zookeeper.connect", getZkConnection());
		return properties;
	}

	public String getBrokerList() {
		return constructBrokerList();
	}

	public String getZkConnection() {
		return this.zookeeper.getConnectionURL();
	}

	@Override
	public String toString() {
		return String.format("EmbeddedKafkaCluster[zk=%s,nodes=%s]", getZkConnection(), getBrokerList());
	}
}
