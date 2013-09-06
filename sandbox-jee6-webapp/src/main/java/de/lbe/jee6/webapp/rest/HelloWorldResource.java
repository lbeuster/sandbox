/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the 
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,  
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.lbe.jee6.webapp.rest;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import de.lbe.jee6.webapp.ejb.Hello;
import de.lbe.jee6.webapp.ejb.HelloService;

/**
 * A simple REST service which is able to say hello to someone using HelloService Please take a look at the web.xml where JAX-RS is enabled
 * 
 * @author gbrey@redhat.com
 * 
 */

@Path("/")
public class HelloWorldResource {

	@Inject
	HelloService helloService;

	@GET
	@Path("/json")
	@Produces({ "application/json" })
	public JsonHello getHelloWorldJSON() {
		Hello message = helloService.createHello("World");
		JsonHello hello = new JsonHello();
		hello.setMessage(message.getMessage());
		hello.setResponseId(message.getId());
		return hello;
	}

	@GET
	@Path("/xml")
	@Produces({ "application/xml" })
	public XmlHello getHelloWorldXML() {
		Hello message = helloService.createHello("World");
		XmlHello hello = new XmlHello();
		hello.setMessage(message.getMessage());
		hello.setResponseId(message.getId());
		return hello;
	}

}
