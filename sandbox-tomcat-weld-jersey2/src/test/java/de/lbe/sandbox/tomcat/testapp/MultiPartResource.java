package de.lbe.sandbox.tomcat.testapp;

import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataParam;

import de.asideas.lib.commons.io.IOUtils;

@Path("/multipart")
public class MultiPartResource {

	@POST
	@Path("/upload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces({ "application/json; charset=UTF-8" })
	public UploadedFile uploadFile(@FormDataParam("file") InputStream fileInputStream, @FormDataParam("file") FormDataBodyPart bodyPart) throws IOException {
		String contentType = JerseyUtils.contentType(bodyPart, null);
		long contentLength = bodyPart.getContentDisposition().getSize();
		String content = IOUtils.toString(fileInputStream);

		UploadedFile file = new UploadedFile();
		file.setContent(content);
		file.setContentLength(contentLength);
		file.setContentType(contentType);
		return file;
	}
}
