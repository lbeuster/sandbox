package de.lbe.sandbox.jboss7;

import java.io.InputStream;
import java.net.URL;

import javax.inject.Singleton;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.zanox.lib.commons.net.httpclient.DefaultHttpClient;
import com.zanox.lib.commons.shrinkwrap.ShrinkWrapUtils;

/**
 * @author lars.beuster
 */
@RunWith(JBossArquillian.class)
public class JBoss7ClientArquillianTest extends Assert {

	@ArquillianResource
	private URL contextPathURL;

	/**
	 * 
	 */
	@Deployment(testable = false)
	public static WebArchive deployment() {
		WebArchive archive = ShrinkWrap.create(WebArchive.class, "managed-test.war");
		ShrinkWrapUtils.addDirectory(archive, "src/test/webapp");
		return archive;
	}

	/**
	 * 
	 */
	@Test
	public void testIndexHtml() throws Exception {
		DefaultHttpClient httpClient = new DefaultHttpClient(this.contextPathURL);
		HttpResponse response = httpClient.GET200("");
		InputStream in = response.getEntity().getContent();
		try {
			String content = IOUtils.toString(in);
			assertEquals("INDEX.HTML", content);
		} finally {
			IOUtils.closeQuietly(in);
		}
	}

	/**
	 * 
	 */
	@Singleton
	public static class TestService {
		// no impl
	}
}
