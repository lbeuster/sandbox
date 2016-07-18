package de.lbe.sandbox.spring.security;

import static com.jayway.restassured.RestAssured.given;
import static javax.servlet.http.HttpServletResponse.SC_OK;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

import org.junit.Test;

import com.jayway.restassured.http.ContentType;

/**
 * @author lbeuster
 */
public class AdminResourceIT extends AbstractSecurityIT {

	@Test
	public void testPingIsPublic() {
		final String response = given().when().get("/admin/ping").then().statusCode(SC_OK).contentType(ContentType.TEXT).extract().asString();
		assertEquals("pong", response);
	}

	@Test
	public void testMetricsIsNotPublic() {
		given().when().get("/admin/metrics").then().statusCode(SC_UNAUTHORIZED);
	}

	@Test
	public void testMetricsIsProtected() {
		givenWithBasicAuth().when().get("/admin/metrics").then().statusCode(SC_OK);
	}
}
