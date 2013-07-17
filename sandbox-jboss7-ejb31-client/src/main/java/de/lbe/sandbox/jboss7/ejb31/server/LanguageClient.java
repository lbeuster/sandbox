package de.lbe.sandbox.jboss7.ejb31.server;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.junit.Test;

/**
 * 
 */
public class LanguageClient {

	@Test
	public void testGetLanguageEntity() throws Exception {
		// Invoke a stateless bean
		LanguageService service = lookupLanguageService();
		Language lang = service.getLanguage(1);
		System.out.println(lang);
	}

	@Test
	public void testGetLanguageInterface() throws Exception {
		// Invoke a stateless bean
		LanguageService service = lookupLanguageService();
		Language lang = service.getLanguage(1);
		System.out.println(lang);
	}

	/**
	 * 
	 */
	private static LanguageService lookupLanguageService() throws NamingException {
		final Hashtable<String, Object> jndiProperties = new Hashtable<String, Object>();
		jndiProperties.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
		final Context context = new InitialContext(jndiProperties);
		String jarJndiName = "ejb:/test-ejb31-server-1.0-SNAPSHOT/LanguageServiceImpl!test.ejb.common.LanguageService";
		String earJndiName =
			"ejb:test-ejb31-ear-jboss71-1.0-SNAPSHOT/test-ejb31-server-1.0-SNAPSHOT/LanguageServiceImpl!test.ejb.common.LanguageService";
		return (LanguageService) context.lookup(earJndiName);
	}
}
