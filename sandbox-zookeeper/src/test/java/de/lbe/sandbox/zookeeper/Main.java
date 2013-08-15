package de.lbe.sandbox.zookeeper;

/**
 * @author lbeuster
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String host = "localhost:2000";
		String znode = "/mynode";
		try {
			DataMonitor dm = new DataMonitor(host, znode);
			Thread.sleep(1_000_000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}