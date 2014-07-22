package de.lbe.sandbox.resteasy.spring.boot;

import de.asideas.lib.commons.resteasy.spring.AbstractResteasyWebAppInitializer;

/**
 * @author lbeuster
 */
public class ResteasySpringWebappInitializer extends AbstractResteasyWebAppInitializer {

	public ResteasySpringWebappInitializer() {
		super(SpringConfiguration.class);
	}
}