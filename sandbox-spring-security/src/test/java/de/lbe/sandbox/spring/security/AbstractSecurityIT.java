package de.lbe.sandbox.spring.security;

import static com.jayway.restassured.RestAssured.given;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Rule;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.jayway.restassured.specification.RequestSpecification;

import de.asideas.ipool.commons.lib.spring.boot.test.AbstractSpringBootIT;
import de.asideas.ipool.commons.lib.spring.boot.test.EmbeddedServletContainerTestSupport;
import de.asideas.ipool.commons.lib.test.restassured.RestAssuredRule;

/**
 * @author lars.beuster
 */
@SpringBootTest(classes = AbstractSecurityIT.TestConfiguration.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class AbstractSecurityIT extends AbstractSpringBootIT {

	@Rule
	public final RestAssuredRule restAssured = new RestAssuredRule();

	@Inject
	private EmbeddedServletContainerTestSupport container;

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
	@Import({ Main.class })
	static class TestConfiguration {
		// only the annotations
	}
}
