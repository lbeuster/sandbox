package de.lbe.sandbox.tomcat;

import java.io.StringReader;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

import org.apache.commons.io.input.ReaderInputStream;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.junit.Test;

import de.lbe.sandbox.tomcat.testapp.UploadedFile;

/**
 * @author lbeuster
 */
public class MultiPartTest extends AbstractJersey2Test {

	/**
	 * 
	 */
	@Test
	public void testUploadFile() throws Exception {

		String content = "CONTENT";
		int contentLength = content.getBytes().length;
		String fileName = "test.txt";

		FormDataMultiPart formDataMultiPart = new FormDataMultiPart();
		final FormDataContentDisposition dispo = FormDataContentDisposition.name("file").fileName(fileName).size(contentLength).build();

		FormDataBodyPart bodyPart = new FormDataBodyPart(dispo, new ReaderInputStream(new StringReader(content)), MediaType.APPLICATION_OCTET_STREAM_TYPE);
		formDataMultiPart.bodyPart(bodyPart);

		// execute
		UploadedFile file =
			this.restClient.target("/rest").path("multipart").path("upload").request().post(Entity.entity(formDataMultiPart, MediaType.MULTIPART_FORM_DATA))
				.readEntity(UploadedFile.class);
		assertEquals(content, file.getContent());
		assertEquals(contentLength, file.getContentLength());
	}
}
