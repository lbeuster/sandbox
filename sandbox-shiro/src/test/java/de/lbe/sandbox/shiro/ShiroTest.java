package de.lbe.sandbox.shiro;

import org.apache.commons.lang3.mutable.MutableObject;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.DisabledSessionException;
import org.apache.shiro.util.ThreadContext;
import org.junit.Before;
import org.junit.Test;

import de.asideas.lib.commons.test.junit.AbstractJUnit4Test;

/**
 * @author lbeuster
 */
public class ShiroTest extends AbstractJUnit4Test {

	private Realm realm;

	private MySecurityManager securityManager;

	private Subject currentUser;

	/**
	 * 
	 */
	@Before
	public void setUp() {
		this.realm = new MyRealm();
		this.securityManager = new MySecurityManager(realm);
		SecurityUtils.setSecurityManager(securityManager);
		this.currentUser = SecurityUtils.getSubject();
	}

	/**
	 * 
	 */
	@Test(expected = DisabledSessionException.class)
	public void testCannotCreateSessions() {
		Session session = currentUser.getSession(true);
	}

	/**
	 * 
	 */
	@Test
	public void testHasRole() {
		currentUser.login(new UsernamePasswordToken(User.ADMIN.getDisplayName(), "egal"));
		assertTrue(currentUser.isAuthenticated());
		assertTrue(currentUser.hasRole(User.ROLE_ADMIN));
		assertTrue(currentUser.isPermitted(User.PERMISSION_TEST));
	}

	/**
	 * 
	 */
	@Test
	public void testAnotherThread() throws Exception  {
		currentUser.login(new UsernamePasswordToken(User.ADMIN.getDisplayName(), "egal"));
		final MutableObject<Subject> subjectInThread = new MutableObject<>();
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				ThreadContext.bind(currentUser);
				try {
					subjectInThread.setValue(ThreadContext.getSubject());
				} finally {
					ThreadContext.unbindSubject();
				}
			}

			protected void runImpl() {
			}
		});
		thread.start();
		thread.join();

		assertSame(this.currentUser, subjectInThread.getValue());
	}
}