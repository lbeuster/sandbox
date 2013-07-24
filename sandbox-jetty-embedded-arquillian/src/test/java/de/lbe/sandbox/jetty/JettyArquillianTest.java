package de.lbe.sandbox.jetty;

import org.apache.http.HttpResponse;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;

import com.zanox.lib.commons.arquillian.AbstractJUnit4ArquillianTest;
import com.zanox.lib.commons.net.httpclient.DefaultHttpClient;
import com.zanox.lib.commons.net.httpclient.HttpClientUtils;
import com.zanox.lib.commons.shrinkwrap.ShrinkWrapUtils;

/**
 * @author lars.beuster
 */
public class JettyArquillianTest extends AbstractJUnit4ArquillianTest {

	/**
	 * defined in arquillian.xml
	 */
	private static final int HTTP_PORT = 9876;

	private static final String CONTEXT_PATH = "mypath";

	/**
	 * 
	 */
	@Deployment
	public static WebArchive deployment1() {
		WebArchive archive = ShrinkWrap.create(WebArchive.class, CONTEXT_PATH + ".war");
		return ShrinkWrapUtils.addDirectory(archive, "src/main/webapp");
	}

	/**
	 * 
	 */
	@Test
	public void testStartJetty() throws Exception {
		DefaultHttpClient client = new DefaultHttpClient("http://localhost:" + HTTP_PORT + "/" + CONTEXT_PATH);
		HttpResponse response = client.GET200("index.html");
		String content = HttpClientUtils.toString(response).trim();
		assertEquals("INDEX.HTML", content);
	}
}
