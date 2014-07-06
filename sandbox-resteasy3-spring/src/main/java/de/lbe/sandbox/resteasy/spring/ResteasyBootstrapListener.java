package de.lbe.sandbox.resteasy.spring;

import org.jboss.resteasy.plugins.server.servlet.ResteasyBootstrap;

/**
 * @author lars.beuster
 */
public class ResteasyBootstrapListener extends ResteasyBootstrap {

	{
		System.out.println(getClass().getName() + ".<init>");
	}
}
