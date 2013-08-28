package de.lbe.sandbox.mongodb.official;

import java.util.Set;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;

/**
 * @author lars.beuster
 */
public class MongoDriverMain {

	private static final String TESTDB = "larsdb";

	public static void main(String[] args) throws Exception {
		MongoClient mongoClient = new MongoClient("localhost", 27017);

		// delete the test db
		DB db = mongoClient.getDB(TESTDB);
		mongoClient.dropDatabase(TESTDB);

		Set<String> tableNames = db.getCollectionNames();
		for (String s : tableNames) {
			System.out.println(s);
		}

		DBCollection testData = db.getCollection("testTable");
		BasicDBObject doc = new BasicDBObject("name", "MongoDB").append("type", "database").append("count", 1)
				.append("info", new BasicDBObject("x", 203).append("y", 102));
		testData.insert(doc);

		BasicDBObject query = new BasicDBObject("count", new BasicDBObject("$gt", 0));
		DBCursor cursor = testData.find(query);
		try {
			while (cursor.hasNext()) {
				System.out.println(cursor.next());
			}
		} finally {
			cursor.close();
		}

		System.out.println(testData.getCount());

	}
}
