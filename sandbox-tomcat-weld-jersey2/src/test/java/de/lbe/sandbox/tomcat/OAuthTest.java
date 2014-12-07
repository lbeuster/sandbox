package de.lbe.sandbox.tomcat;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.apache.connector.ApacheConnectorProvider;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.oauth1.ConsumerCredentials;
import org.glassfish.jersey.client.oauth1.OAuth1ClientSupport;
import org.glassfish.jersey.filter.LoggingFilter;
import org.junit.Before;
import org.junit.Test;

import de.asideas.lib.commons.test.junit.AbstractJUnit4Test;

/**
 * @author lars.beuster
 */
public class OAuthTest extends AbstractJUnit4Test {

	protected Client restClient;

	/**
	 * socialEndpoint: http://iideas-ipool02.asv.local:9090/api/v3/social
	 *
	 * oauthConfiguration: socialKey: social-key socialSecret: social-secret
	 */

	@Before
	public void setUp() {
		ClientConfig config = new ClientConfig();
		config.register(new LoggingFilter());
		config.connectorProvider(new ApacheConnectorProvider());

		ConsumerCredentials credentials = new ConsumerCredentials("social-key", "social-secret");
		Feature oauth = OAuth1ClientSupport.builder(credentials).signatureMethod("HMAC-SHA1").version("1.0").feature().build();

		config.register(oauth);

		this.restClient = ClientBuilder.newClient(config);
	}

	@Test
	public void testOAuth() {
		WebTarget target =
			this.restClient
				.target("http://iideas-ipool01.asv.local:9090/api/v3/social/search?sources=twitter&category=fromCelebrity&celebrityCategory=international&sort=qrelevance&order=desc&limit=10");
		Response response = target.request().get();
		System.out.println(response.getStatus());

		// ConsumerCredentials credentials = new ConsumerCredentials("social-key", "social-secret");
		// Feature build = OAuth1ClientSupport.builder(credentials).signatureMethod("HMAC-SHA1").version("1.0").feature().build();

	}
}
