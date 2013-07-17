package de.lbe.sandbox.jboss7.ejb31;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.junit.Assert;
import org.junit.Test;

import de.lbe.sandbox.jboss7.ejb31.server.Language;
import de.lbe.sandbox.jboss7.ejb31.server.LanguageService;

/**
 * @author lars.beuster
 */
//@RunWith(JBossArquillian.class)
public class SimpleArquillianTest extends Assert {
	
	/**
	 * 
	 */
//	@Deployment(testable = false)
//	public static JavaArchive deployment1() {
//
//		// we create the persistence jar
//		JavaArchive archive = ShrinkWrap.create(JavaArchive.class);
//		ShrinkWrapUtils.addArchiveOfClass(archive, LanguageService.class);
//		return archive;
//	}

	/**
	 * 
	 */
	@Test
	public void testModuleClassLoader() throws Exception {
		LanguageService service = lookupLanguageService2();
		Language lang = service.getLanguage(6);
		assertNotNull(lang);
		assertEquals(6, lang.getId());
		assertNotNull(service);
		System.out.println(service);
	}

	/**
	 * 
	 */
	private static LanguageService lookupLanguageService2() throws NamingException {
		final Hashtable<String, Object> jndiProperties = new Hashtable<String, Object>();
		jndiProperties.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
		final Context context = new InitialContext(jndiProperties);
		String jarJndiName = "ejb:/test-ejb31-server-1.0-SNAPSHOT/LanguageServiceImpl!test.ejb.common.LanguageService";
		String earJndiName =
			"ejb:test-ejb31-ear-jboss71-1.0-SNAPSHOT/test-ejb31-server-1.0-SNAPSHOT/LanguageServiceImpl!test.ejb.common.LanguageService";
		earJndiName = "ejb:/sandbox-jboss7-ejb31-client-1.0-SNAPSHOT/Language_ServiceImpl!de.lbe.sandbox.jboss7.ejb31.server.LanguageService";
		return (LanguageService) context.lookup(earJndiName);
	}
	
	/**
	 * 
	 */
	private static LanguageService lookupLanguageService() throws NamingException {
		
		final Hashtable<String, Object> jndiProperties = new Hashtable<String, Object>();
		jndiProperties.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
//		jndiProperties.put("remote.connectionprovider.create.options.org.xnio.Options.SSL_ENABLED", "false");
//		jndiProperties.put("remote.connections", "default");
//		jndiProperties.put("remote.connection.default.host", "localhost");
//		jndiProperties.put("remote.connection.default.port", "4447");
//		jndiProperties.put("remote.connection.default.connect.options.org.xnio.Options.SASL_POLICY_NOANONYMOUS", "false");
		
		final Context context = new InitialContext(jndiProperties);
		
		String jarJndiName = "ejb:/sandbox-jboss7-ejb31-client-1.0-SNAPSHOT/Language_ServiceImpl!de.lbe.sandbox.jboss7.ejb31.server.LanguageService";
//		String earJndiName =
//			"ejb:test-ejb31-ear-jboss71-1.0-SNAPSHOT/test-ejb31-server-1.0-SNAPSHOT/LanguageServiceImpl!test.ejb.common.LanguageService";
		return (LanguageService) context.lookup(jarJndiName);
	}
	
}
