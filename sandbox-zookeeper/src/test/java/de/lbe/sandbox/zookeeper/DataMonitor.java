package de.lbe.sandbox.zookeeper;

/**
 * A simple class that monitors the data and existence of a ZooKeeper
 * node. It uses asynchronous ZooKeeper APIs.
 */
import java.io.IOException;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

public class DataMonitor implements Watcher {

	private ZooKeeper zk;

	private String znode;

	byte prevData[];

	public DataMonitor(String host, String znode) throws IOException {
		this.znode = znode;
		this.zk = new ZooKeeper(host, 3000, this);

		// read data and install a watch on the node
		readNodeData();
	}

	/**
	 * 
	 */
	@Override
	public void process(WatchedEvent event) {
		String path = event.getPath();
		System.out.println("process: " + event);
		if (path != null && path.equals(znode)) {
			// Something has changed on the node, we have to reestablish out watch
			readNodeData();
		}
	}

	/**
	 * 
	 */
	private void readNodeData() {
		try {
			Stat stat = zk.exists(this.znode, this);
			if (stat == null) {
				// node doesn't exist
				System.out.println("node doesn't exist");
			} else {
				// we should only read the data in certain events (no NodeDeleted or something)
				byte[] bytes = zk.getData(znode, false, stat);
				System.out.println("data=" + new String(bytes));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}