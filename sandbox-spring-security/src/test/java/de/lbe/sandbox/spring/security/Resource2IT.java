package de.lbe.sandbox.spring.security;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

import org.junit.Test;

/**
 * @author lars.beuster
 */
public class Resource2IT extends AbstractSecurityIT {

	@Test
	public void testProtecedPath2IsAuthenticated() {
		String response = given().auth().preemptive().basic("user2", "pass2").get("/path2/protected").then().statusCode(200).extract().asString();
		assertThat(response, containsString("user2"));
		assertThat(response, containsString("role2"));
	}

	@Test
	public void testProtecedPath2IsUnauthenticated() {
		given().get("/path2/protected").then().statusCode(401);
	}

	@Test
	public void testPublicPath2IsPublic() {
		String response = given().get("/path2/public").then().statusCode(200).extract().asString();
		assertEquals("success", response);
	}
}
