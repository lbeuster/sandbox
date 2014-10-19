package de.lbe.sandbox.springboot.old;

import java.util.Collections;
import java.util.List;

import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.asideas.lib.commons.tomcat.embedded.EmbeddedTomcat;
import de.asideas.lib.commons.tomcat.embedded.EmbeddedTomcatInitializer;

/**
 * @author lbeuster
 */
@Component
public class EmbeddedTomcatBooter {

	/**
	 * Doesn't work via method/constructor injection if the list is empty
	 */
	@Autowired(required = false)
	private List<EmbeddedTomcatInitializer> initializers = Collections.emptyList();

	private final EmbeddedTomcatFactory factory;

	private EmbeddedTomcat tomcat = null;

	/**
	 * 
	 */
	@Autowired
	public EmbeddedTomcatBooter(EmbeddedTomcatFactory factory) {
		this.factory = factory;
	}

	/**
	 * This method has to called manually.
	 */
	public EmbeddedTomcat boot() {

		if (this.tomcat != null) {
			throw new IllegalStateException("Tomcat already started");
		}

		// init tomcat
		EmbeddedTomcat tomcat = this.factory.create();
		for (EmbeddedTomcatInitializer initializer : this.initializers) {
			tomcat.addContainerInitializer(initializer);
		}

		// start
		tomcat.start();
		this.tomcat = tomcat;
		startDaemonAwaitThread();
		return this.tomcat;
	}

	/**
	 * 
	 */
	@PreDestroy
	public void stop() {
		if (this.tomcat != null) {
			this.tomcat.stop();
			this.tomcat = null;
		}
	}

	/**
	 * 
	 */
	public EmbeddedTomcat getTomcat() {
		if (this.tomcat == null) {
			throw new IllegalStateException("Tomcat not started");
		}
		return this.tomcat;
	}

	/**
	 * 
	 */
	private void startDaemonAwaitThread() {
		Thread awaitThread = new Thread("Tomcat-await") {
			@Override
			public void run() {
				// maybe already stopped because of start exceptions
				if (tomcat != null) {
					tomcat.await();
				}
			}
		};
		awaitThread.setDaemon(false);
		awaitThread.start();
	}
}
