package de.lbe.sandbox.kafka;

/**
 * https://cwiki.apache.org/confluence/display/KAFKA/0.8.0+Producer+Example
 */
public class TestConfig {

	public static final String TOPIC_NAME = "TestProducer";

	public static final String CONSUMER_GROUP = "AnyGroup";

	/**
	 * We don't have to specify all - just one available server.
	 */
	static final String BROKERS = "localhost:9092";

	static final String ZOOKEEPER = "localhost:2181";
}
