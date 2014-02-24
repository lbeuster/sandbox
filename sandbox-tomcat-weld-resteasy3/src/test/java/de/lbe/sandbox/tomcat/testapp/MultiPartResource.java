package de.lbe.sandbox.tomcat.testapp;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/multipart")
public class MultiPartResource {

	@POST
	@Path("/upload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces({ "application/json; charset=UTF-8" })
	public UploadedFile uploadFile() {
		return null;
	}
}
