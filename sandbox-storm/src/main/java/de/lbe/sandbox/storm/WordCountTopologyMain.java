package de.lbe.sandbox.storm;

import java.util.Arrays;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;

/**
 * This topology demonstrates Storm's stream groupings and multilang capabilities.
 */
public class WordCountTopologyMain {

	public static void main(String[] args) throws Exception {
		System.out.println("main..." + Arrays.toString(args));

		TopologyBuilder builder = new TopologyBuilder();

		builder.setSpout("spout", new RandomSentenceSpout(), 5);

		builder.setBolt("split", new SplitSentenceBolt(), 8).shuffleGrouping("spout");
		builder.setBolt("count", new WordCountBolt(), 12).fieldsGrouping("split", new Fields("word"));

		Config conf = new Config();
		conf.setDebug(true);
		conf.put("hallo", "HALLO");

		if (args != null && args.length > 0) {
			conf.setNumWorkers(3);

			StormSubmitter.submitTopologyWithProgressBar(args[0], conf, builder.createTopology());
		} else {
			conf.setMaxTaskParallelism(3);

			LocalCluster cluster = new LocalCluster();
			cluster.submitTopology("word-count", conf, builder.createTopology());

			Thread.sleep(10000);

			cluster.shutdown();
		}
	}
}
