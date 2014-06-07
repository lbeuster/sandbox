package de.lbe.sandbox.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

import de.asideas.lib.commons.test.junit.AbstractJUnit4Test;

/**
 * @author lbeuster
 */
public class ShiroTest extends AbstractJUnit4Test {

	/**
	 * 
	 */
	@Test
	public void test() {

		Realm realm = new MyRealm();
		DefaultSecurityManager securityManager = new DefaultSecurityManager(realm);
		SecurityUtils.setSecurityManager(securityManager);

		// get the currently executing user:
		Subject currentUser = SecurityUtils.getSubject();
		System.out.println(currentUser.isAuthenticated());

		UsernamePasswordToken token = new UsernamePasswordToken("lars1", "pwd");
		currentUser.login(token);
		System.out.println(currentUser.isAuthenticated());
		
		System.out.println(currentUser.hasRole(User.ROLE_ADMIN));
		System.out.println(currentUser.isPermitted("perm"));

	}
}