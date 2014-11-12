package de.lbe.sandbox.spring.data;

import javax.inject.Inject;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.test.context.ContextConfiguration;

import com.mongodb.Mongo;

import de.asideas.lib.commons.spring.test.AbstractJUnitSpringIT;
import de.asideas.lib.commons.test.mongodb.EmbeddedMongo;

/**
 * @author lbeuster
 */
@ContextConfiguration(classes = AbstractTest.TestConfiguration.class)
public abstract class AbstractTest extends AbstractJUnitSpringIT {

	@Configuration
	@ComponentScan
	@EnableMongoRepositories
	static class TestConfiguration {

		@Bean(initMethod = "start", destroyMethod = "stop")
		EmbeddedMongo embeddedMongo() {
			EmbeddedMongo mongo = new EmbeddedMongo();
			mongo.setPort(7031);
			return mongo;
		}
	}

	@Configuration
	static class MongoConfiguration extends AbstractMongoConfiguration {

		@Inject
		private EmbeddedMongo embeddedMongo;

		@Override
		protected String getDatabaseName() {
			return "sandbox-test";
		}

		@Override
		public Mongo mongo() throws Exception {
			return embeddedMongo.getClient();
		}
	}
}