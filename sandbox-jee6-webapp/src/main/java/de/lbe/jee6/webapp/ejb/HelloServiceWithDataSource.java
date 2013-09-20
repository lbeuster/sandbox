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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.sql.DataSource;

import de.asideas.lib.commons.sql.SQLUtils;

/**
 * 
 */
@Stateless
public class HelloServiceWithDataSource {

	@Resource
	private DataSource dataSource;

	public Hello createHello(long id, String name) {
		String message = "Hello " + name + "!";
		Hello hello = new Hello();
		hello.setId(id);
		hello.setMessage(message);
		Connection connection = null;
		Statement statement = null;
		try {
			connection = dataSource.getConnection();
			statement = connection.createStatement();
			int result = statement.executeUpdate(String.format("insert into Hello (id, message) values (%s, '%s')", id, message));
			if (result != 1) {
				throw new RuntimeException("result!=1 (" + result + ")");
			}

			// TomEE: we always get a different connection, but all connections are collected in the current TX
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		} finally {
			SQLUtils.closeQuietly(statement);
			SQLUtils.closeQuietly(connection);
		}
		return hello;
	}

	public Collection<Hello> findAllHellos() {
		findAllHellosImpl();
		findAllHellosImpl();
		Collection<Hello> hellos = findAllHellosImpl();
		return hellos;
	}

	private Collection<Hello> findAllHellosImpl() {

		List hellos = new ArrayList();

		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			statement = connection.createStatement();
			resultSet = statement.executeQuery("select id, message from Hello");
			while (resultSet.next()) {
				int id = resultSet.getInt(1);
				String message = resultSet.getString(2);
				Hello hello = new Hello();
				hello.setId(id);
				hello.setMessage(message);
				hellos.add(hello);
			}
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		} finally {
			SQLUtils.closeQuietly(resultSet);
			SQLUtils.closeQuietly(statement);
			SQLUtils.closeQuietly(connection);
		}

		return hellos;
	}

}
