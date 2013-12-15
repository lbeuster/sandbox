package de.lbe.sandbox.tomcat;

import java.io.File;
import java.util.Set;

import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.CDI;

import org.apache.catalina.core.StandardContext;
import org.apache.catalina.deploy.ContextResource;
import org.apache.catalina.deploy.ContextTransaction;
import org.apache.catalina.startup.Tomcat;
import org.apache.naming.factory.BeanFactory;
import org.apache.naming.resources.VirtualDirContext;
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

		String webappDirLocation = "src/main/webapp/";
		Tomcat tomcat = new Tomcat();
		tomcat.setPort(9000);
		tomcat.enableNaming();

		StandardContext ctx = (StandardContext) tomcat.addWebapp("/", new File(webappDirLocation).getAbsolutePath());
		// tomcat.addServlet(ctx, "jersey", new JerseyServlet());

		// ctx.addServletMapping("/U", "jersey");

		// declare an alternate location for your "WEB-INF/classes" dir:
		File additionWebInfClasses = new File("target/test-classes");
		VirtualDirContext resources = new VirtualDirContext();
		resources.setExtraResourcePaths("/WEB-INF/classes=" + additionWebInfClasses);
		ctx.setResources(resources);

		configureTransactions(ctx);
		
		// doesn't work
		configureCDI(ctx);

		// Context ctx = tomcat.addWebapp("/", new File(".").getAbsolutePath());
		// ((StandardJarScanner) ctx.getJarScanner()).setScanAllFiles(true);

		// FilterDef def = new FilterDef();
		// def.setFilterClass(Listener.class.getName());
		// ctx.addFilterDef(def);

		tomcat.start();
		tomcat.getServer().await();
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
