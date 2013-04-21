package de.lbe.sandbox.jetty;

import java.io.File;

import org.apache.http.HttpResponse;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.importer.ExplodedImporter;
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
		WebArchive archive = ShrinkWrap.create(WebArchive.class, CONTEXT_PATH);
		// archive.setWebXML(new File("src/main/webapp/WEB-INF/web.xml"));
		// archive.add(new FileAsset(new File("src/main/webapp/index.html")), "index.html");
		// archive.addAsDirectory("src/main/webapp/index.html");
		archive = ShrinkWrapUtils.addDirectory(archive, "src/main/webapp");
//
//		archive = archive.as(ExplodedImporter.class)
//
//		.importDirectory("src/main/webapp")
//
//		.as(WebArchive.class);
		return archive;
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
