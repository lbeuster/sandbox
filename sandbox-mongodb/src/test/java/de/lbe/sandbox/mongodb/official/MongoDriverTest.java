package de.lbe.sandbox.mongodb.official;

import java.net.URI;
import java.util.Set;

import javax.net.ssl.SSLSocketFactory;

import org.junit.Test;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import de.asideas.lib.commons.net.QueryString;
import de.asideas.lib.commons.net.ssl.SSLUtils;
import de.asideas.lib.commons.test.junit.AbstractJUnit4Test;
import de.asideas.lib.commons.util.convert.TypeConverter;

/**
 * @author lars.beuster
 */
public class MongoDriverTest extends AbstractJUnit4Test {

	private static final String TESTDB = "larsdb";

	/**
	 * 
	 */
	@Test
	public void testSSL() throws Exception {

		SSLSocketFactory installInsecureSocketFactoryForJSSE = SSLUtils.installInsecureSocketFactoryForJSSE();

		// String uri = "mongodb://celepedia_test:FJrFoVKhytcmbPuYyDFuuNt4nSB3@mongosoup-dedicated-as-pre01.mongosoup.de:27017/celepedia_test?replicaSet=rs_asideas-pre";
		// String uri =
		// "mongodb://celepedia_test:FJrFoVKhytcmbPuYyDFuuNt4nSB3@mongosoup-dedicated-as-pre01.mongosoup.de:27017,mongosoup-dedicated-as-pre02.mongosoup.de:27017,mongosoup-dedicated-as-pre03.mongosoup.de:27017/celepedia_test?replicaSet=rs_asideas-pre&ssl=true";

		String uri = "mongodb://localhost";
		MongoClientOptionsBuilder builder = new MongoClientOptionsBuilder().socketFactory(installInsecureSocketFactoryForJSSE).freezeSocketFactory();

		URI _uri = new URI(uri);
		System.out.println(_uri.getQuery());
		QueryString qs = QueryString.parse(_uri.getQuery());
		String sslParam = qs.getParameter("ssl");
		boolean ssl = TypeConverter.toBoolean(sslParam, false);
		System.out.println(ssl);

		MongoClientURI mongoURI = new MongoClientURI(uri, builder);

		MongoClient mongoClient = new MongoClient(mongoURI);

		DB db = mongoClient.getDB("celepedia_test");

		Set<String> tableNames = db.getCollectionNames();
		for (String s : tableNames) {
			System.out.println(s);
		}

	}

	/**
	 * 
	 */
	public void test() throws Exception {

		// SSLSocketFactory installInsecureSocketFactoryForJSSE = SSLUtils.installInsecureSocketFactoryForJSSE();
		//
		// String uri =
		// "mongodb://celepedia_test:FJrFoVKhytcmbPuYyDFuuNt4nSB3@mongosoup-dedicated-as-pre01.mongosoup.de:27017,mongosoup-dedicated-as-pre02.mongosoup.de:27017,mongosoup-dedicated-as-pre03.mongosoup.de:27017/celepedia_test?replicaSet=rs_asideas-pre&ssl=true";
		//
		// MongoClientOptions.Builder builder = new MongoClientOptions.Builder();
		// builder.socketFactory(installInsecureSocketFactoryForJSSE);
		//
		// MongoClientURI mongoURI = new MongoClientURI(uri, builder);
		// MongoClient mongoClient = new MongoClient(mongoURI);
		//
		// if (true) {
		// return;
		// }

		MongoClient mongoClient = null;

		// delete the test db
		DB db = mongoClient.getDB(TESTDB);
		mongoClient.dropDatabase(TESTDB);

		Set<String> tableNames = db.getCollectionNames();
		for (String s : tableNames) {
			System.out.println(s);
		}

		DBCollection testData = db.getCollection("testTable");
		BasicDBObject doc = new BasicDBObject("name", "MongoDB").append("type", "database").append("count", 1).append("info", new BasicDBObject("x", 203).append("y", 102));
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
