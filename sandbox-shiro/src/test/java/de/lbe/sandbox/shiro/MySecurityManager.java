package de.lbe.sandbox.shiro;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.AuthorizingSecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionException;
import org.apache.shiro.session.mgt.SessionContext;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.SubjectContext;
import org.apache.shiro.subject.support.DelegatingSubject;

/**
 * @author lbeuster
 */
public class MySecurityManager extends AuthorizingSecurityManager {
	
	private final UserRepository userRepository = new UserRepository();

	public MySecurityManager(Realm realm) {
		setRealm(realm);
	}

	@Override
	public Subject login(Subject subject, AuthenticationToken authenticationToken) throws AuthenticationException {
		UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
		User user = this.userRepository.getUserByDisplayName(token.getUsername());
		PrincipalCollection principals = new SimplePrincipalCollection(token.getUsername(), getRealms().iterator().next().getName());
		return new DelegatingSubject(principals, user != null, "MyHost", null, false, this);
	}

	@Override
	public void logout(Subject subject) {

	}

	@Override
	public Subject createSubject(SubjectContext context) {
		return new DelegatingSubject(this);
	}

	@Override
	public Session start(SessionContext context) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Session getSession(SessionKey key) throws SessionException {
		throw new UnsupportedOperationException();
	}

}