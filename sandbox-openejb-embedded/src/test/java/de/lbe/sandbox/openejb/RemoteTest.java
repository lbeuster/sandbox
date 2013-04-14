package de.lbe.sandbox.openejb;

import java.util.Properties;

import javax.annotation.ManagedBean;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.junit.Test;

/**
 * @author lbeuster
 */
@ManagedBean
public class RemoteTest extends AbstractLanguageTest {

	/**
	 * 
	 */
	@Test
	public void testRemoteAccess() throws Exception {

		Properties properties = new Properties();
		properties.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.openejb.client.RemoteInitialContextFactory");
		properties.setProperty(Context.PROVIDER_URL, "ejbd://localhost:4201");
		InitialContext remoteContext = new InitialContext(properties);

		LanguageService service = (LanguageService) remoteContext.lookup(LanguageServiceImpl.class.getSimpleName() + "Remote");

		service.getLanguage(1);
	}
}
