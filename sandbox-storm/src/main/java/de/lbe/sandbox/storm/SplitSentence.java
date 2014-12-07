package de.lbe.sandbox.storm;

import java.util.Collections;
import java.util.Map;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;

public class SplitSentence extends BaseBasicBolt {

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("word"));
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		return null;
	}

	@Override
	public void execute(Tuple input, BasicOutputCollector collector) {
		String[] words = ((String) input.getValue(0)).split(" ");
		for (Object word : words) {
			collector.emit(Collections.singletonList(word));
		}
	}
}
