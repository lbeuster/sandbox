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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import de.lbe.jee6.webapp.ejb.Hello;
import de.lbe.jee6.webapp.ejb.SingletonHelloService;

/**
 * A simple REST service which is able to say hello to someone using HelloService Please take a look at the web.xml where JAX-RS is enabled
 * 
 */

@Path("/")
public class HelloWorldResource {

	@Inject
	SingletonHelloService helloService;

	@GET
	@Path("/hellos/get/json")
	@Produces({ "application/json" })
	public List<JsonHello> getHellos() {

		Collection<Hello> hellos = this.helloService.findAllHellos();

		List<JsonHello> jsonHellos = new ArrayList<JsonHello>();
		for (Hello hello : hellos) {
			JsonHello jsonHello = new JsonHello();
			jsonHello.setMessage(hello.getMessage());
			jsonHello.setResponseId(hello.getId());
			jsonHellos.add(jsonHello);
		}
		return jsonHellos;
	}

	@GET
	@Path("/hellos/get/xml")
	@Produces({ "application/xml" })
	public XmlHello getHelloWorldXML() {
		return null;

	}

	@GET
	@Path("/hellos/create/{id}/{message}")
	@Produces({ "application/json" })
	public JsonHello createHello(@PathParam("id") long id, @PathParam("message") String message) {

		Hello hello = this.helloService.createHello(id, message);

		JsonHello jsonHello = new JsonHello();
		jsonHello.setResponseId(hello.getId());
		jsonHello.setMessage(hello.getMessage());
		return jsonHello;
	}

}
