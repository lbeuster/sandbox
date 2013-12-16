package de.lbe.sandbox.aws;

import java.io.File;

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
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.util.StringInputStream;

import de.asideas.lib.commons.io.IOUtils;
import de.asideas.lib.commons.net.NetUtils;
import de.asideas.lib.commons.test.junit.AbstractJUnit4Test;

/**
 * @author lbeuster
 */
public class AWSTest extends AbstractJUnit4Test {

	private static final String BUCKET_NAME = "celepedia-dev";

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
	public void testSomething() throws Exception {

		// URL url = new URL("http://celepedia-dev.s3.amazonaws.com");
		// System.out.println(IOUtils.toString(url));

		String key = "lars/lars-test";
		String content = "HALLO";

		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(content.length());
		PutObjectResult putObject = client.putObject(new PutObjectRequest(BUCKET_NAME, key, new StringInputStream(content), metadata));

		// try {
		// S3Object object = client.getObject(BUCKET_NAME, key + "56");
		// S3ObjectInputStream objectContent = object.getObjectContent();
		// System.out.println("CONTenT: " + IOUtils.toString(objectContent));
		// } catch (Exception ex) {
		// ex.printStackTrace();
		// }
	}
}