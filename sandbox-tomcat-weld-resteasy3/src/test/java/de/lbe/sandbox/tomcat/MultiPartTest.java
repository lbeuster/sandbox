package de.lbe.sandbox.tomcat;

import org.junit.Test;

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
		int contentLength = content.getBytes().length;
		String fileName = "test.txt";

		// FormDataMultiPart formDataMultiPart = new FormDataMultiPart();
		// final FormDataContentDisposition dispo = FormDataContentDisposition.name("file").fileName(fileName).size(contentLength).build();
		//
		// FormDataBodyPart bodyPart = new FormDataBodyPart(dispo, new ReaderInputStream(new StringReader(content)), MediaType.APPLICATION_OCTET_STREAM_TYPE);
		// formDataMultiPart.bodyPart(bodyPart);
		//
		// // execute
		// UploadedFile file =
		// this.restClient.target("/rest").path("multipart").path("upload").request().post(Entity.entity(formDataMultiPart, MediaType.MULTIPART_FORM_DATA))
		// .readEntity(UploadedFile.class);
		// assertEquals(content, file.getContent());
		// assertEquals(contentLength, file.getContentLength());
	}
}
