package de.lbe.sandbox.springboot;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.junit.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

import de.asideas.ipool.commons.lib.spring.boot.test.AbstractSpringBootIT;
import de.lbe.sandbox.springboot.conf.MyConfig;

/**
 * @author lbeuster
 */
// @ContextConfiguration(classes = { PropertiesAsMapTest.TestConfiguration.class })
@SpringApplicationConfiguration(classes = AbstractSpringBootTest.TestConfiguration.class)
@WebAppConfiguration
public class PropertiesAsMapTest extends AbstractSpringBootIT {

	@Inject
	private MyConfig testProperties;

	/**
	 *
	 */
	@Test
	public void testAsMap() {
		System.out.println(testProperties);
	}

	/**
	 *
	 */
	@Configuration
	@EnableAutoConfiguration
	// @EnableConfigurationProperties
	@Import({ TestProperties.class, MyConfig.class })
	@ComponentScan
	static class TestConfiguration {
	}

	/**
	 *
	 */
	@ConfigurationProperties(prefix = "lars", ignoreUnknownFields = false)
	@Component
	public static class TestProperties {

		private String name;

		private Map<String, Object> info = new HashMap<String, Object>();

		public Map<String, Object> getInfo() {
			return info;
		}

		public void setInfo(Map<String, Object> info) {
			this.info = info;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}
}
