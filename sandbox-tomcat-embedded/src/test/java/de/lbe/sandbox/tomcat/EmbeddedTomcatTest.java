package de.lbe.sandbox.tomcat;

import java.util.Set;

import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.CDI;

import org.apache.catalina.core.StandardContext;
import org.apache.catalina.deploy.ContextResource;
import org.apache.catalina.deploy.ContextTransaction;
import org.apache.naming.factory.BeanFactory;
import org.jboss.weld.resources.ManagerObjectFactory;
import org.junit.Test;

import com.atomikos.icatch.jta.UserTransactionFactory;
import com.atomikos.icatch.jta.UserTransactionManager;

import de.lbe.sandbox.tomcat.testapp.TestService;
import de.asideas.lib.commons.cdi.BeanManagerUtils;
import de.asideas.lib.commons.test.junit.AbstractJUnit4Test;

/**
 * @author lars.beuster
 */
public class EmbeddedTomcatTest extends AbstractJUnit4Test {

	@Test
	public void testStart() throws Exception {

		TomcatBooter tomcat = new TomcatBooter();
		tomcat.setWebappDir("src/main/webapp");
		tomcat.setExtraClassesDir("target/test-classes");
		tomcat.start();

		// configureTransactions(ctx);

		//
		// EmbeddedTestTomcat tomcat = new EmbeddedTestTomcat();
		//
		// tomcat.start();

		BeanManager beanManager = CDI.current().getBeanManager();
		Set<Bean<?>> beans = beanManager.getBeans(Object.class);
		// System.out.println(CollectionUtils.toStringLines(beans));

		// BeanManager beanManager = BeanManagerProvider.getInstance().getBeanManager();
		TestService service = BeanManagerUtils.getContextualReference(beanManager, TestService.class);
		service.service();

	}

	private void configureTransactions(StandardContext ctx) {
		// UserTransaction
		ContextTransaction tx = new ContextTransaction();
		tx.setProperty("factory", UserTransactionFactory.class.getName());
		ctx.getNamingResources().setTransaction(tx);

		// TX-Manager
		ContextResource txManager = new ContextResource();
		txManager.setAuth("Container");
		txManager.setType(UserTransactionManager.class.getName());
		txManager.setName("TransactionManager");
		txManager.setProperty("factory", BeanFactory.class.getName());
		ctx.getNamingResources().addResource(txManager);
	}

	private void configureCDI(StandardContext ctx) {
		ContextResource beanManager = new ContextResource();
		beanManager.setAuth("Container");
		beanManager.setType(BeanManager.class.getName());
		beanManager.setName("BeanManager");
		beanManager.setProperty("factory", ManagerObjectFactory.class.getName());
		ctx.getNamingResources().addResource(beanManager);
	}
	// private File deployment() {
	// WebArchive war = ShrinkWrap.create(WebArchive.class);
	// // ShrinkWrapUtils.addDirectory(war, "src/test/webapp");
	// war.addClass(TestServlet.class);
	// File file = new File("target/test.war");
	// ShrinkWrapUtils.exportToFile(war, "target/test.war", true);
	// return file;
	// }
}
