package de.lbe.sandbox.tomcat.testapp;

import java.io.IOException;
import java.util.jar.Manifest;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.asideas.lib.commons.lang.ExecutableJarClassLoader;
import de.asideas.lib.commons.lang.StringUtils;
import de.asideas.lib.commons.util.jar.ManifestUtils;

/**
 * @author lbeuster
 */
@WebServlet(name = "VersionServlet", urlPatterns = "/admin/version", loadOnStartup = 100)
public class VersionServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@Override
	public void init() {
		System.out.println(getClass().getName() + ".init()");
	}

	/**
	 * 
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

		// we need the MANIFEST of a JAR that belongs to the application (not to any used lib)
		// we expect that our application is started from an executable jar with the JarClassLoader in it
		Manifest manifest = ManifestUtils.getManifestOfClass(ExecutableJarClassLoader.class);
		String builtDate = ManifestUtils.getBuiltDateAsString(manifest);
		String version = ManifestUtils.getImplementationVersion(manifest);
		String title = ManifestUtils.getImplementationVendorId(manifest);

		// write the response
		String text = StringUtils.format("%s:%s (%s)", title, version, builtDate);
		response.getWriter().println(text);
	}
}
