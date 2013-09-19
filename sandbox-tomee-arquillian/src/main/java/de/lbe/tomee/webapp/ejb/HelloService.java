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
package de.lbe.tomee.webapp.ejb;

import java.util.Collection;
import java.util.Collections;

import javax.ejb.Stateless;

/**
 * 
 */
@Stateless
public class HelloService {

	public Hello createHello(String name) {
		String message = "Hello " + name + "!";
		Hello hello = new Hello();
		hello.setMessage(message);
		return hello;
	}

	public Collection<Hello> findAllHellos() {
		return Collections.emptyList();
	}

}
