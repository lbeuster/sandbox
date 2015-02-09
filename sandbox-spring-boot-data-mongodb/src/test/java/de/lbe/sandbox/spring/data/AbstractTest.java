package de.lbe.sandbox.spring.data;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import de.asideas.ipool.commons.lib.spring.boot.test.AbstractSpringBootIT;
import de.asideas.ipool.commons.lib.test.mongo.EmbeddedMongo;

/**
 * @author lbeuster
 */
@SpringApplicationConfiguration(classes = AbstractTest.TestConfiguration.class)
public abstract class AbstractTest extends AbstractSpringBootIT {

	@Configuration
	@ComponentScan
	@EnableMongoRepositories
	@EnableAutoConfiguration
	static class TestConfiguration {

		@Bean(initMethod = "start", destroyMethod = "stop")
		EmbeddedMongo embeddedMongo() {
			EmbeddedMongo mongo = new EmbeddedMongo();
			mongo.setPort(7031);
			return mongo;
		}
	}
}