package de.lbe.sandbox.mongodb.embedded;

import java.util.Date;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;

/**
 * 
 */
public class EmbeddedMongoTest {
	private static final String DATABASE_NAME = "embedded";

	private Mongo mongo;

	public static void main(String[] args) throws Exception {

		int port = 12345;
		IMongodConfig mongodConfig = new MongodConfigBuilder().version(Version.Main.PRODUCTION).net(new Net(port, Network.localhostIsIPv6())).build();

		MongodStarter runtime = MongodStarter.getDefaultInstance();

		MongodExecutable mongodExecutable = null;
		try {
			mongodExecutable = runtime.prepare(mongodConfig);
			MongodProcess mongod = mongodExecutable.start();

			MongoClient mongo = new MongoClient("localhost", port);
			DB db = mongo.getDB("test");
			DBCollection col = db.createCollection("testCol", new BasicDBObject());
			col.save(new BasicDBObject("testDoc", new Date()));

		} finally {
			if (mongodExecutable != null)
				mongodExecutable.stop();
		}
	}
}
