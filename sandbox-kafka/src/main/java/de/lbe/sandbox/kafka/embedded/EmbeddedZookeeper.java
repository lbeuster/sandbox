package de.lbe.sandbox.kafka.embedded;

import kafka.utils.TestUtils;
import kafka.utils.ZKStringSerializer$;

import org.I0Itec.zkclient.ZkClient;

import com.google.common.base.Verify;

import de.asideas.lib.commons.lang.ShutdownHook;

/**
 * Why don't we use the kafka.zk.EmbeddedZookeeper? Because it has no support for getting any free port - you would have to prepare that before.
 *
 * @see http://pannoniancoder.blogspot.de/2014/08/embedded-kafka-and-zookeeper-for-unit.html
 * @see https://gist.github.com/vmarcinko/e4e58910bcb77dac16e9
 *
 * @author lbeuster
 */
public class EmbeddedZookeeper {

	private kafka.zk.EmbeddedZookeeper delegate;

	private final String hostname = "localhost";

	private int port = -1;

	private ZkClient client;

	private boolean started = false;

	private ShutdownHook shutdownHook;

	public EmbeddedZookeeper() {
		this(-1);
	}

	public EmbeddedZookeeper(int port) {
		this.port = port;
	}

	/**
	 *
	 */
	public void start() {

		this.started = true;

		// resolve the port
		if (this.port == -1) {
			this.port = TestUtils.choosePort();
		}

		// start ZK
		this.delegate = new kafka.zk.EmbeddedZookeeper(getConnectionURL());

		// install shutdown hook
		this.shutdownHook = new ShutdownHook("ZooKeeper-Shutdown-" + getConnectionURL()) {
			@Override
			public void run() {
				shutdown();
			}
		};
	}

	/**
	 *
	 */
	public ZkClient client() {
		assertIsStarted();
		if (this.client == null) {
			// the $MODULE stuff is important
			this.client = new ZkClient(getConnectionURL(), 5_000, 5_000, ZKStringSerializer$.MODULE$);
		}
		return this.client;
	}

	/**
	 *
	 */
	public void shutdown() {
		if (this.shutdownHook != null) {
			this.shutdownHook.uninstall();
		}
		if (this.delegate != null) {
			this.delegate.shutdown();
		}
	}

	private void assertIsStarted() {
		Verify.verify(this.started, "EmbeddedZookeeper has not been started");
	}

	public String getConnectionURL() {
		return this.delegate != null ? this.delegate.connectString() : this.hostname + ":" + port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("EmbeddedZookeeper{");
		sb.append("connection=").append(getConnectionURL());
		sb.append('}');
		return sb.toString();
	}
}
