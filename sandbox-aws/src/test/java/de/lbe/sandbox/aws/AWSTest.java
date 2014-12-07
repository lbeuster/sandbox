package de.lbe.sandbox.aws;

import java.io.File;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.SystemUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.StringInputStream;

import de.asideas.ipool.commons.lib.net.NetUtils;
import de.asideas.ipool.commons.lib.test.junit.AbstractJUnitTest;

/**
 * @author lbeuster
 */
public class AWSTest extends AbstractJUnitTest {

	private AmazonS3Client client;

	/**
	 *
	 */
	@Before
	public void setUp() throws Exception {

		// NetUtils.setHttpProxyHost("proxy.service.asv.local");
		// NetUtils.setHttpProxyPort(8080);
		// NetUtils.setHttpsProxyHost("proxy.service.asv.local");
		// NetUtils.setHttpsProxyPort(8080);

		System.out.println(NetUtils.httpProxyHost());
		System.out.println(NetUtils.httpNonProxyHosts());
		System.out.println(NetUtils.httpsProxyHost());
		System.out.println(NetUtils.httpsNonProxyHosts());

		File home = new File(SystemUtils.USER_DIR);
		String accessKeyId = IOUtils.toString(new File(home, "aws.id").toURI());
		String secretKey = IOUtils.toString(new File(home, "aws.secret").toURI());
		AWSCredentials credentials = new BasicAWSCredentials(accessKeyId, secretKey);

		ClientConfiguration config = new ClientConfiguration();
		config.setProxyHost("proxy.service.asv.local");
		config.setProxyPort(8080);

		// thread safe: https://forums.aws.amazon.com/thread.jspa?threadID=50723#
		this.client = new AmazonS3Client(credentials, config);
	}

	/**
	 *
	 */
	@After
	public void tearDown() {
		// optional operation
		this.client.shutdown();
	}

	/**
	 *
	 */
	@Test
	public void testReadAccessOnStaging() throws Exception {
		String key = "53d263c5e4b0febc9a4053d0";
		S3Object object = this.client.getObject("staging.s3.celepedia.de", key);
		assertNotNull(object);
	}

	/**
	 *
	 */
	@Test
	public void testReadAccessOnEmbedded() throws Exception {
		String key = "53f224e4e4b087e131d1a861";
		S3Object object = this.client.getObject("embedded.s3.celepedia.de", key);
		assertNotNull(object);
	}

	/**
	 *
	 */
	@Test
	public void testReadAccessOnProduction() throws Exception {
		String key = "53f21f6de4b0f60a2bf0e271";
		S3Object object = this.client.getObject("production.s3.celepedia.de", key);
		assertNotNull(object);
	}

	/**
	 *
	 */
	@Test
	public void testWriteAccessOnStaging() throws Exception {
		String key = "larstest";
		ObjectMetadata metadata = new ObjectMetadata();
		String content = "LARSTEST";
		metadata.setContentLength(content.length());
		client.putObject(new PutObjectRequest("staging.s3.celepedia.de", key, new StringInputStream(content), metadata));
	}

	/**
	 *
	 */
	@Test
	public void testWriteAccessOnEmbedded() throws Exception {
		String key = "larstest";
		ObjectMetadata metadata = new ObjectMetadata();
		String content = "LARSTEST";
		metadata.setContentLength(content.length());
		client.putObject(new PutObjectRequest("embedded.s3.celepedia.de", key, new StringInputStream(content), metadata));
	}

	/**
	 *
	 */
	@Test
	public void testWriteAccessOnProduction() throws Exception {
		String key = "larstest";
		ObjectMetadata metadata = new ObjectMetadata();
		String content = "LARSTEST";
		metadata.setContentLength(content.length());
		client.putObject(new PutObjectRequest("production.s3.celepedia.de", key, new StringInputStream(content), metadata));
	}
}