package de.lbe.sandbox.tomcat.weld.spi;

import javax.transaction.Synchronization;
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
		throw new UnsupportedOperationException();
	}

}
