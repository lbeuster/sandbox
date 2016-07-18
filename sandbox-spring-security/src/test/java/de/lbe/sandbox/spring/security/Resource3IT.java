package de.lbe.sandbox.spring.security;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

import org.junit.Test;

/**
 * @author lars.beuster
 */
public class Resource3IT extends AbstractSecurityIT {

	@Test
	public void testProtecedPath2IsAuthenticated() {
		String response = given().auth().preemptive().basic("user3", "pass3").get("/path3/protected").then().statusCode(200).extract().asString();
		assertThat(response, containsString("user3"));
		assertThat(response, containsString("role3"));
	}

	@Test
	public void testProtecedPathIsUnauthenticated() {
		given().get("/path3/protected").then().statusCode(401);
	}

	@Test
	public void testPublicPathIsPublic() {
		String response = given().get("/path3/public").then().statusCode(200).extract().asString();
		assertEquals("success", response);
	}
}
