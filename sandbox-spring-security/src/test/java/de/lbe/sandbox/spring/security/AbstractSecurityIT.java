package de.lbe.sandbox.spring.security;

import static com.jayway.restassured.RestAssured.given;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Rule;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.web.WebAppConfiguration;

import com.jayway.restassured.specification.RequestSpecification;

import de.asideas.ipool.commons.lib.spring.boot.MoreSpringBootAutoConfiguration;
import de.asideas.ipool.commons.lib.spring.boot.context.embedded.EmbeddedServletContainerSupport;
import de.asideas.ipool.commons.lib.spring.boot.test.AbstractSpringBootIT;
import de.asideas.ipool.commons.lib.test.restassured.RestAssuredRule;

/**
 * @author lars.beuster
 */
@SpringApplicationConfiguration(classes = { AbstractSecurityIT.TestConfiguration.class })
@WebAppConfiguration
public class AbstractSecurityIT extends AbstractSpringBootIT {

	@Rule
	public final RestAssuredRule restAssured = new RestAssuredRule();

	@Inject
	private EmbeddedServletContainerSupport container;

	@Inject
	private SecurityProperties securityProperties;

	private String username;
	private String password;

	/**
	 *
	 */
	@Before
	public void setUpRest() throws Exception {

		username = securityProperties.getUser().getName();
		password = securityProperties.getUser().getPassword();

		// remove existing API keys created by previous test

		// enable detailed log messages when validation fails
		restAssured.setBaseUri(container.getWebappContextURL());
	}

	/**
	 *
	 */
	protected RequestSpecification givenWithBasicAuth() {
		return given().auth().preemptive().basic(username, password);
	}

	@Configuration
	// @formatter:off
	@Import({
		MoreSpringBootAutoConfiguration.class,
		Main.class
	})
	// @formatter:on
	static class TestConfiguration {
		//
	}
}
