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
package de.lbe.jee6.webapp.ejb;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

/**
 * A simple CDI service which is able to say hello to someone
 * 
 * @author Pete Muir
 * 
 */
@Stateless
public class HelloService {

	@PersistenceContext(name = "Test-JEE6")
	private EntityManager entityManager;

	public Hello createHello(String name) {
		String message = "Hello " + name + "!";
		Hello hello = new Hello();
		hello.setMessage(message);
		this.entityManager.persist(hello);
		return hello;
	}

	public Collection<Hello> findAllHellos() {
		CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Hello> query = cb.createQuery(Hello.class);
		TypedQuery<Hello> typedQuery = this.entityManager.createQuery(query);
		return typedQuery.getResultList();
	}

}
