package de.lbe.sandbox.storm.spring;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import de.lbe.sandbox.storm.StringService;

@Configuration
@EnableConfigurationProperties(TestProperties.class)
@Import(StringService.class)
public class TestConfiguration {

	@Bean
	@ConditionalOnMissingBean(TestService.class)
	public TestService testService() {
		return new TestService();
	}

	@Bean
	@ConditionalOnMissingBean(TestService.class)
	public TestService testService2() {
		return new TestService();
	}

}
