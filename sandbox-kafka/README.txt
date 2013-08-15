http://kafka.apache.org/documentation.html#quickstart
	* Download kafka
	* Start zookeeper server
		bin/zookeeper-server-start.sh config/zookeeper.properties
	* Start kafka server
		bin/kafka-server-start.sh config/server.properties
	* List topics
		bin/kafka-list-topic.sh --zookeeper localhost:2181
	* Create the test topic
		bin/kafka-create-topic.sh --topic TestProducer --replica 1 --zookeeper localhost:2181 --partition 5