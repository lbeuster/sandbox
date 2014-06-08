package de.lbe.sandbox.shiro;

import org.apache.shiro.authc.Account;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAccount;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

/**
 * @author lbeuster
 */
public class MyRealm extends AuthorizingRealm {

	private final UserRepository userRepository = new UserRepository();

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		Account account = (Account) principals.getPrimaryPrincipal();
		return account;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

		UsernamePasswordToken userToken = (UsernamePasswordToken) token;

		String displayName = userToken.getUsername();
		String password = new String(userToken.getPassword());
		User user = userRepository.findByDisplayName(displayName);
		if (user == null) {
			throw new AuthenticationException("user not found");
		}
		Account account = newAccount(user);
		return new SimpleAuthenticationInfo(account, password, getName());
	}

	private Account newAccount(User user) {
		SimpleAccount account = new SimpleAccount(user, null, getName());
		account.addRole(user.getRoles());
		account.addStringPermission(User.PERMISSION_TEST);
		return account;
	}
}