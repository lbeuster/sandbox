package de.lbe.sandbox.splunk;

import org.junit.Test;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.exception.FacebookOAuthException;
import com.restfb.types.User;

import de.asideas.lib.commons.lang.ObjectUtils;
import de.asideas.lib.commons.test.junit.AbstractJUnit4Test;

/**
 * @author lars.beuster
 */
public class FacebookTest extends AbstractJUnit4Test {

	private static final String ACCESS_TOKEN =
		"CAAM3WfNzOKkBAIefFFPb52AyeZAHTpZBaSKfS2SGfof9OcSlZBRt9zEMjdcrll2Lg7V9NXvAiGWV0c5cRQroayDhz1AW2Nn3UiCHlDf3d1e6sOK7DZAL5BKCX7MNyzYpxQGTi1b8JdK4dtHgHf0W9gWfdThgbyApRZCq7a66BCZCV0rcxPf8am";

	/**
	 * 
	 */
	@Test
	public void testMe() {
		FacebookClient facebookClient = new DefaultFacebookClient(ACCESS_TOKEN);
		User user = facebookClient.fetchObject("me", User.class);
		System.out.println(ObjectUtils.dump(user));
	}

	/**
	 * 
	 */
	@Test(expected = FacebookOAuthException.class)
	public void testInvalidToken() {
		FacebookClient facebookClient = new DefaultFacebookClient("invalid-token");
		facebookClient.fetchObject("me", User.class);
	}
}
