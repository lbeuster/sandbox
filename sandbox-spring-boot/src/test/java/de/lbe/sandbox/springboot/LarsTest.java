package de.lbe.sandbox.springboot;

import org.junit.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import de.asideas.lib.commons.test.junit.AbstractJUnit4Test;
import de.lbe.sandbox.springboot.conf.MyConfig;

public class LarsTest extends AbstractJUnit4Test {

	@Test
	public void testLars() {
		SpringApplication app = new SpringApplication(TestConfiguration.class);
		app.setShowBanner(false);
		app.setWebEnvironment(false);
		// if (activeProfile != null) {
		// app.setAdditionalProfiles(this.activeProfile);
		// }
		ConfigurableApplicationContext context = app.run();
		MyConfig config = context.getBean(MyConfig.class);
		assertNotNull(config);
		context.close();
	}

	@Configuration
	@Import(MyConfig.class)
	@EnableAutoConfiguration
	public static class TestConfiguration {

	}

}
