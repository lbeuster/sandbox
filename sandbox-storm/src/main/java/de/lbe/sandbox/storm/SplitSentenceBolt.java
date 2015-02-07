package de.lbe.sandbox.storm;

import java.util.Collections;
import java.util.Map;

import javax.inject.Inject;

import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;

import de.asideas.ipool.commons.lib.storm.AbstractSpringAwareBasicBolt;
import de.asideas.ipool.commons.lib.util.MoreMaps;

public class SplitSentenceBolt extends AbstractSpringAwareBasicBolt {

	@Inject
	private StringService service;

	@Override
	public void prepare(Map stormConf, TopologyContext context) {
		System.out.println("------------------------------------------------------");
		System.out.println(getClass());
		System.out.println(MoreMaps.toString(stormConf, "{\n\t", "\n\t", "\n}"));
		System.out.println("------------------------------------------------------");
		super.prepare(stormConf, context);
	}

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
		String[] words = this.service.split((String) input.getValue(0));
		for (Object word : words) {
			collector.emit(Collections.singletonList(word));
		}
	}
}
