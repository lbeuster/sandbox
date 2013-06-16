package de.lbe.weld;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.SessionScoped;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.weld.context.bound.BoundSessionContextImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.zanox.lib.commons.shrinkwrap.ShrinkWrapUtils;

/**
 * @author Lars Beuster
 */
public class SessionScopeTest extends AbstractWeldTest {

	/**
     *
     */
	private BoundSessionContextImpl sessionContext = null;

	/**
	 * 
	 */
	private Map<String, Object> sessionObjects;

	/**
	 * 
	 */
	@Deployment
	public static JavaArchive createDeployment() {
		return ShrinkWrapUtils.prepareCdiJar();
	}

	/**
     *
     */
	@Before
	public void initSessionContext() {
		this.sessionObjects = new HashMap<String, Object>();

		this.sessionContext = new BoundSessionContextImpl();
		this.sessionContext.associate(this.sessionObjects);
		this.sessionContext.activate();

		this.beanManager.addContext(this.sessionContext);
	}

	/**
     *
     */
	@After
	public void destroySessionContext() {
		this.sessionContext.cleanup();
	}

	/**
     *
     */
	@Test
	public void testSessionScope() {

		// get 2 beans - we should get a reference to the same instance
		SessionScopedTestService service1 = getBean(SessionScopedTestService.class);
		SessionScopedTestService service2 = getBean(SessionScopedTestService.class);
		assertNull(service1.getName());
		assertNull(service2.getName());

		// set the name on one service - it should be set on the other service
		final String NAME = "NAME";
		service1.setName(NAME);
		assertEquals(NAME, service1.getName());
		assertEquals(NAME, service2.getName());
	}

	/**
	 * Serializable because of SessionScope.
	 */
	@SuppressWarnings("serial")
	@SessionScoped
	public static class SessionScopedTestService implements Serializable {

		private String name;

		public String getName() {
			return this.name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}
}
