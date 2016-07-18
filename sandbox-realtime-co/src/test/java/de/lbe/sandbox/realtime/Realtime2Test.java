package de.lbe.sandbox.realtime;
/**
 * @fileoverview This file contains the integration tests for the Ortc Api
 * @author ORTC team members (ortc@ibt.pt)
 */

import java.util.HashMap;
import java.util.LinkedList;

import ibt.ortc.api.ChannelPermissions;
import ibt.ortc.api.Ortc;
import ibt.ortc.api.Proxy;
import ibt.ortc.extensibility.OnConnected;
import ibt.ortc.extensibility.OnDisconnected;
import ibt.ortc.extensibility.OnException;
import ibt.ortc.extensibility.OnMessage;
import ibt.ortc.extensibility.OnReconnected;
import ibt.ortc.extensibility.OnReconnecting;
import ibt.ortc.extensibility.OnSubscribed;
import ibt.ortc.extensibility.OnUnsubscribed;
import ibt.ortc.extensibility.OrtcClient;
import ibt.ortc.extensibility.OrtcFactory;

public class Realtime2Test {

	private static final String defaultPrivateKey = "kMIJtK3Mtmmv";
	private static final boolean defaultNeedsAuthentication = false;
	private static String serverUrl = "http://ortc-developers.realtime.co/server/2.1";
	private static boolean isBalancer = true;
	private static String applicationKey = "58bFZC";
	private static String authenticationToken = "RealtimeDemo";
	private static Proxy proxy;

	private static long startTime;

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		System.out.println("Welcome to Realtime Demo");
		OrtcClient client = connectOrtcClient();
		Thread.sleep(2000);
		String channel = "larstest";
		subscribeChannel(client, channel);
		send(client, channel);
		System.out.println("WAITING...");

		// client.unsubscribe(channel);
		// client.disconnect();
	}

	static long sent = 0;

	/**
	 *
	 */
	private static void send(OrtcClient client, String channel) {
		startTime = System.currentTimeMillis();
		Thread t = new Thread() {
			@Override
			public void run() {
				while (true) {
					try {
						// Thread.sleep(10);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					client.send(channel, "HALLO");
					sent++;
					if (sent % 100 == 0) {
						long duration = (System.currentTimeMillis() - startTime) / 1000;
						if (duration == 0) {
							duration = 1;
						}
						long throughput = sent / duration;
						System.out.println("sent " + sent + ", throughput: " + throughput);

					}
				}
			}
		};
		t.start();
	}

	private static long recieved = 0;

	private static void subscribeChannel(OrtcClient client, String channel) {
		client.subscribe(channel, true, new OnMessage() {
			@Override
			public void run(OrtcClient sender, String channel, String message) {
				recieved++;
				if (recieved % 100 == 0) {
					long duration = (System.currentTimeMillis() - startTime) / 1000;
					if (duration == 0) {
						duration = 1;
					}
					long throughput = recieved / duration;
					System.out.println("recieved " + recieved + ", throughput: " + throughput);
				}
				// System.out.println(String.format("Message received on channel %s: '%s'", channel, message));

				if ("unsubscribe".equals(message)) {
					sender.unsubscribe(channel);
				} else if ("disconnect".equals(message)) {
					sender.disconnect();
				}
			}
		});
	}

	private static OrtcClient connectOrtcClient() throws Exception {
		if (defaultNeedsAuthentication) {
			System.out.println("Authenticating...");

			HashMap<String, LinkedList<ChannelPermissions>> permissions = new HashMap<String, LinkedList<ChannelPermissions>>();

			LinkedList<ChannelPermissions> yellowPermissions = new LinkedList<ChannelPermissions>();
			yellowPermissions.add(ChannelPermissions.Write);
			yellowPermissions.add(ChannelPermissions.Presence);

			LinkedList<ChannelPermissions> testPermissions = new LinkedList<ChannelPermissions>();
			testPermissions.add(ChannelPermissions.Read);
			testPermissions.add(ChannelPermissions.Presence);

			permissions.put("yellow:*", yellowPermissions);
			permissions.put("test:*", testPermissions);

			if (Ortc.saveAuthentication(serverUrl, isBalancer, authenticationToken, false, applicationKey, 14000, defaultPrivateKey, permissions, proxy)) {
				System.out.println("Authentication successful");
			} else {
				System.out.println("Unable to authenticate");
			}
		}

		Ortc api = new Ortc();

		OrtcFactory factory = api.loadOrtcFactory("IbtRealtimeSJ");

		final OrtcClient client = factory.createClient();

		if (System.getProperty("http.proxyHost") != null) {
			System.out.println("Main.connectOrtcClient: using proxy");
			proxy = new Proxy(System.getProperty("http.proxyHost"), Integer.parseInt(System.getProperty("http.proxyPort")), System.getProperty("http.proxyUser"),
				System.getProperty("http.proxyPassword"));
			client.setProxy(proxy);
		}
		client.setHeartbeatActive(false);

		if (isBalancer) {
			client.setClusterUrl(serverUrl);
		} else {
			client.setUrl(serverUrl);
		}

		System.out.println(String.format("Connecting to server %s", serverUrl));

		client.onConnected = new OnConnected() {
			@Override
			public void run(OrtcClient sender) {
				System.out.println(String.format("Connected to %s", client.getUrl()));
			}
		};

		client.onException = new OnException() {
			@Override
			public void run(OrtcClient send, Exception ex) {
				System.out.println(String.format("Error: '%s'", ex.toString()));
			}
		};

		client.onDisconnected = new OnDisconnected() {
			@Override
			public void run(OrtcClient sender) {
				System.out.println("Disconnected");
			}
		};

		client.onReconnected = new OnReconnected() {
			@Override
			public void run(OrtcClient sender) {
				System.out.println(String.format("Reconnected to %s", client.getUrl()));
			}
		};

		client.onReconnecting = new OnReconnecting() {
			@Override
			public void run(OrtcClient sender) {
				System.out.println(String.format("Reconnecting to %s", client.getUrl()));
			}
		};

		client.onSubscribed = new OnSubscribed() {
			@Override
			public void run(OrtcClient sender, String channel) {
				System.out.println(String.format("Subscribed to channel %s", channel));

			}
		};

		client.onUnsubscribed = new OnUnsubscribed() {
			@Override
			public void run(OrtcClient sender, String channel) {
				System.out.println(String.format("Unsubscribed from channel %s", channel));
			}
		};

		System.out.println("Connecting...");
		client.setConnectionMetadata("JavaApp");
		client.setHeartbeatActive(true);
		client.connect(applicationKey, authenticationToken);
		return client;
	}
}
