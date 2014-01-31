package de.lbe.sandbox.tomcat;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.Synchronization;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.jboss.weld.transaction.spi.TransactionServices;

/**
 * We have the problem that the maven-surefire-plugin creates a classpath with one JarFile in it that references all other classpath elements via the MANIFEST.MF. But the original
 * scanner cannot handle this classloader.
 * <p/>
 * We can test if we still need this call by uncommenting its use in the jetty-maven-module and executing the tests. If all works fine we can remove it. Perhaps it could be
 * sufficient to execute the tests in the tomcat-maven-module but I haven't tried that.
 * 
 * @author Lars Beuster
 */
public class TransactionServicesImpl implements TransactionServices {

	@Override
	public void cleanup() {
		// TODO Auto-generated method stub

	}

	@Override
	public void registerSynchronization(Synchronization synchronizedObserver) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isTransactionActive() {
		System.out.println(getClass().getSimpleName() + ".isTransactionActive()");
		return false;
	}

	@Override
	public UserTransaction getUserTransaction() {
		System.out.println(getClass().getSimpleName() + ".getUserTransaction()");
		return new UserTransaction() {

			@Override
			public void setTransactionTimeout(int seconds) throws SystemException {
				// TODO Auto-generated method stub

			}

			@Override
			public void setRollbackOnly() throws IllegalStateException, SystemException {
				// TODO Auto-generated method stub

			}

			@Override
			public void rollback() throws IllegalStateException, SecurityException, SystemException {
				// TODO Auto-generated method stub

			}

			@Override
			public int getStatus() throws SystemException {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public void commit() throws RollbackException, HeuristicMixedException, HeuristicRollbackException, SecurityException, IllegalStateException, SystemException {
				// TODO Auto-generated method stub

			}

			@Override
			public void begin() throws NotSupportedException, SystemException {
				// TODO Auto-generated method stub

			}
		};
		// throw new UnsupportedOperationException();
	}

}
