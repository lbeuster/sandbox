package de.lbe.sandbox.kafka;

import kafka.producer.Partitioner;
import kafka.utils.VerifiableProperties;

public class SimplePartitioner implements Partitioner<String> {

	public SimplePartitioner(VerifiableProperties props) {

	}

	public int partition(String key, int a_numPartitions) {
		return Math.abs(key.hashCode()) % a_numPartitions;
	}

}