package de.lbe.sandbox.shiro;

import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.SubjectDAO;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionException;
import org.apache.shiro.session.mgt.SessionContext;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.SubjectContext;

/**
 * @author lbeuster
 */
public class MySecurityManager extends DefaultSecurityManager {

	public MySecurityManager(Realm realm) {
		super(realm);
		setSessionManager(new SessionManager() {

			@Override
			public Session start(SessionContext context) {
				throw new UnsupportedOperationException();
			}

			@Override
			public Session getSession(SessionKey key) throws SessionException {
				throw new UnsupportedOperationException();
			}
		});
		setSubjectDAO(new SubjectDAO() {

			@Override
			public Subject save(Subject subject) {
				return subject;
			}

			@Override
			public void delete(Subject subject) {
			}
		});
	}

	protected SubjectContext resolveSession(SubjectContext context) {
		context = super.resolveSession(context);
		context.setSessionCreationEnabled(false);
		context.setSession(null);
		return context;

	}
}