package de.lbe.sandbox.tomcat.testapp;

import javax.activation.FileTypeMap;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.media.multipart.BodyPart;
import org.glassfish.jersey.media.multipart.ContentDisposition;

/**
 * @author lbeuster
 */
public class JerseyUtils {

	/**
	 * 
	 */
	public static String contentType(BodyPart bodyPart, String defaultContentType) {

		// maybe the header was not provided by the client
		if (bodyPart == null) {
			return defaultContentType;
		}

		String contentType = null;
		MediaType mediaType = bodyPart.getMediaType();
		if (mediaType != null) {
			contentType = mediaType.toString();
		}

		// try content disposition (perhaps with filename)
		if (!isUsefulContentType(contentType)) {
			ContentDisposition contentDisposition = bodyPart.getContentDisposition();
			contentType = contentType(contentDisposition, defaultContentType);
		}

		// use the default unknown
		if (!isUsefulContentType(contentType)) {
			contentType = defaultContentType;
		}
		return contentType;
	}

	/**
	 * 
	 */
	public static String contentType(ContentDisposition contentDispositionHeader, String defaultContentType) {

		// maybe the header was not provided by the client
		if (contentDispositionHeader == null) {
			return defaultContentType;
		}

		// maybe filename was not provided
		String fileName = contentDispositionHeader.getFileName();
		if (fileName == null) {
			return defaultContentType;
		}

		// use javax.activation
		String contentType = FileTypeMap.getDefaultFileTypeMap().getContentType(fileName);
		if (!isUsefulContentType(contentType)) {
			contentType = defaultContentType;
		}
		return contentType;
	}

	/**
	 * 
	 */
	private static boolean isUsefulContentType(String contentType) {
		return contentType != null && !contentType.equals(unknownContentType());
	}

	/**
	 * 
	 */
	private static String unknownContentType() {
		return MediaType.APPLICATION_OCTET_STREAM;
	}
}
