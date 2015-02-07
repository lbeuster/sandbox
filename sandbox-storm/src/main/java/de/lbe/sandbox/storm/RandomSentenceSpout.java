/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.lbe.sandbox.storm;

import java.util.Map;
import java.util.Random;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;

import de.asideas.ipool.commons.lib.storm.AbstractSpringAwareRichSpout;
import de.asideas.ipool.commons.lib.util.MoreMaps;

public class RandomSentenceSpout extends AbstractSpringAwareRichSpout {
	SpoutOutputCollector _collector;
	Random _rand;

	@Override
	public void open(Map stormConf, TopologyContext context, SpoutOutputCollector collector) {
		super.open(stormConf, context, collector);
		_collector = collector;
		_rand = new Random();
		System.out.println("------------------------------------------------------");
		System.out.println(getClass());
		System.out.println(MoreMaps.toString(stormConf, "{\n\t", "\n\t", "\n}"));
		System.out.println("------------------------------------------------------");
	}

	@Override
	public void nextTuple() {
		System.out.println("------------------------------------------------------");
		System.out.println("next sentence");
		// System.out.println(ClassLoaders.toString(getClass().getClassLoader()));
		System.out.println("------------------------------------------------------");
		String[] sentences = new String[] { "one two" };
		String sentence = sentences[_rand.nextInt(sentences.length)];
		_collector.emit(new Values(sentence));
		Utils.sleep(5000);
	}

	@Override
	public void ack(Object id) {
	}

	@Override
	public void fail(Object id) {
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("word"));
	}

}