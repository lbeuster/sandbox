package de.lbe.sandbox.tomcat.testapp;

import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import de.asideas.lib.commons.resteasy.common.ContentDisposition;
import de.asideas.lib.commons.resteasy.multipart.MultiPartUtils;

@Path("/multipart")
public class MultiPartResource {

	@POST
	@Path("/upload")
	@Consumes("multipart/form-data")
	@Produces({ "application/json; charset=UTF-8" })
	public UploadedFile uploadFile(MultipartFormDataInput input) throws IOException {

		UploadedFile file = new UploadedFile();

		InputPart inputPart = MultiPartUtils.extractFileInputPart(input);
		ContentDisposition contentDisposition = MultiPartUtils.parseContentDispositionFromMultiPart(inputPart);
		MediaType contentType = inputPart.getMediaType();

		// Handle the body of that part with an InputStream
		InputStream istream = inputPart.getBody(InputStream.class, null);
		String content = IOUtils.toString(istream);
		IOUtils.closeQuietly(istream);

		file.setContent(content);
		file.setFileName(contentDisposition.getFileName());
		file.setContentType(contentType.getType() + "/" + contentType.getSubtype());

		return file;
	}
}
