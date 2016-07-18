package de.lbe.sandbox.spring.data;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Date;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.core.convert.CustomConversions;
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

		@Bean
		public CustomConversions customConversions() {
			return new CustomConversions(Arrays.asList(DateToZonedDateTimeConverter.INSTANCE, ZonedDateTimeToDateConverter.INSTANCE));
		}
	}

	public static enum DateToZonedDateTimeConverter implements Converter<Date, ZonedDateTime> {

		INSTANCE;

		@Override
		public ZonedDateTime convert(Date source) {
			// we always convert to local TZ because we lost the original TZ during the conversion to Date
			return source == null ? null : ZonedDateTime.ofInstant(source.toInstant(), ZoneId.systemDefault());
		}
	}

	public static enum ZonedDateTimeToDateConverter implements Converter<ZonedDateTime, Date> {

		INSTANCE;

		@Override
		public Date convert(ZonedDateTime source) {
			// to have a consitent value we always use UTC
			return source == null ? null : Date.from(source.toInstant());
		}
	}

}