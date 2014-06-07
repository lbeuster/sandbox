package de.lbe.sandbox.shiro;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAccount;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

/**
 * @author lbeuster
 */
public class MyRealm extends AuthorizingRealm {

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		Object primaryPrincipal = principals.getPrimaryPrincipal();
		return null;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		SimpleAccount account = new SimpleAccount(token.getPrincipal(), token.getCredentials(), getName());
		account.addRole(User.ROLE_ADMIN);
		account.addStringPermission("perm");
		return account;
	}
}