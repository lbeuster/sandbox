package de.lbe.sandbox.tomcat;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataOutput;
import org.junit.Test;

import de.lbe.sandbox.tomcat.testapp.UploadedFile;

/**
 * @author lbeuster
 */
public class MultiPartTest extends AbstractRestTest {

	/**
	 * 
	 */
	@Test
	public void testUploadFile() throws Exception {

		String content = "CONTENT";
		String fileName = "test.txt";
		InputStream in = new ByteArrayInputStream(content.getBytes());

		MediaType mediaType = MediaType.TEXT_PLAIN_TYPE;
		String formDataKey = "file";

		MultipartFormDataOutput multipart = new MultipartFormDataOutput();
		multipart.addFormData(formDataKey, in, mediaType, fileName);
		GenericEntity<MultipartFormDataOutput> multiPartEntity = new GenericEntity<MultipartFormDataOutput>(multipart) {
			// super constructor is protected
		};
		Entity<?> postEntity = Entity.entity(multiPartEntity, MediaType.MULTIPART_FORM_DATA);

		UploadedFile uploadedFile = prepareClient().path("/multipart/upload").request().post(postEntity, UploadedFile.class);

		assertEquals(fileName, uploadedFile.getFileName());
		assertEquals(content, uploadedFile.getContent());
		assertEquals(mediaType.toString(), uploadedFile.getContentType());
	}
}
