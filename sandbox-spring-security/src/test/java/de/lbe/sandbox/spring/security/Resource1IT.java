package de.lbe.sandbox.spring.security;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

import org.junit.Test;

/**
 * @author lars.beuster
 */
public class Resource1IT extends AbstractSecurityIT {

	@Test
	public void testProtecedPath1IsAuthenticated() {
		String response = given().auth().preemptive().basic("user1", "pass1").get("/path1/protected").then().statusCode(200).extract().asString();
		assertThat(response, containsString("user1"));
		assertThat(response, containsString("role1"));
	}

	@Test
	public void testProtecedPath1IsUnauthenticated() {
		given().get("/path1/protected").then().statusCode(401);
	}

	@Test
	public void testPublicPath1IsPublic() {
		String response = given().auth().preemptive().basic("user", "pass").get("/path1/public").then().statusCode(200).extract().asString();
		assertEquals("success", response);
	}
}
