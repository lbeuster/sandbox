package de.lbe.sandbox.tomcat;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;

import org.apache.tomcat.JarScanner;
import org.apache.tomcat.JarScannerCallback;
import org.apache.tomcat.util.scan.StandardJarScanner;

import de.asideas.lib.commons.io.FileUtils;
import de.asideas.lib.commons.lang.Assert;
import de.asideas.lib.commons.lang.StringUtils;
import de.asideas.lib.commons.util.CollectionUtils;

/**
 * We have the problem that the maven-surefire-plugin creates a classpath with one JarFile in it that references all other classpath elements via the
 * MANIFEST.MF. But the original scanner cannot handle this classloader.
 * <p/>
 * We can test if we still need this call by uncommenting its use in the jetty-maven-module and executing the tests. If all works fine we can remove
 * it. Perhaps it could be sufficient to execute the tests in the tomcat-maven-module but I haven't tried that.
 * 
 * @author Lars Beuster
 */
public class SurefireJarScanner extends StandardJarScanner {

	/**
     *
     */
	public static void install(ServletContext servletContext) {
		Assert.isNotNull(servletContext, "servletContext");
		servletContext.setAttribute(JarScanner.class.getName(), new SurefireJarScanner());
	}

	/**
     *
     */
	@Override
	public void scan(ServletContext context, ClassLoader classloader, JarScannerCallback callback, Set<String> jarsToSkip) {

		// the surefire-plugin sets since 2.9 the java.class.path variable to the test-classpath. This is somehow the
		// reason that the classes are in 2 classloaders: the AppClassLoader and the Surefire-CL which may result in
		// ClassCastExceptions - especially if we set surefire.useManifestOnlyJar=false. But the only reason to do this
		// is because the original JarScanner cannot handle the useManifestOnlyJar=true - but we can

		// create the temporary classloader from the java.class.path system-property
		String classpathAsString = System.getProperty("java.class.path");
		if (StringUtils.isNotEmpty(classpathAsString)) {

			List<URL> urls = new ArrayList<>();

			// first add the URLs of the original CL
			if (classloader instanceof URLClassLoader) {
				URLClassLoader urlClassloader = (URLClassLoader) classloader;
				CollectionUtils.addAll(urls, urlClassloader.getURLs());
			}

			// convert the classpath-string to an array of URLs
			String[] classpathEntries = StringUtils.toStringArray(classpathAsString, File.pathSeparator);
			for (String classpathEntry : classpathEntries) {
				File file = new File(classpathEntry).getAbsoluteFile();
				URL url = FileUtils.toURL(file);
				urls.add(url);
			}

			// create the CL
			URLClassLoader tmpCL = new URLClassLoader(urls.toArray(new URL[urls.size()]));

			// manipulate the contextCL to be used in the super-class-method
			// Tomcat 7.0.37- used the Thread-CL as a starting CL
			// Tomcat 7.0.38+ uses the CL given as a param to our method - but this is the URL-CL with only one generated Surefire Jar
			ClassLoader originalContextCL = Thread.currentThread().getContextClassLoader();
			Thread.currentThread().setContextClassLoader(tmpCL);

			// call the original method
			try {
				super.scan(context, tmpCL, callback, jarsToSkip);
			} finally {
				if (originalContextCL != null) {
					Thread.currentThread().setContextClassLoader(originalContextCL);
				}
			}
		}
	}
}
