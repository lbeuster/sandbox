/*
 * Copyright 2012-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.lbe.sandbox.spring.sleuth;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.cloud.sleuth.Trace;
import org.springframework.cloud.sleuth.TraceScope;
import org.springframework.cloud.sleuth.TraceTemplate;
import org.springframework.web.client.RestTemplate;

import de.asideas.ipool.commons.lib.spring.boot.test.AbstractSpringBootIT;

/**
 * @author lbeuster
 */
@SpringApplicationConfiguration(classes = SleuthApplication.class)
@WebIntegrationTest
public class SleuthIT extends AbstractSpringBootIT {

	@Inject
	private RestTemplate restTemplate;

	@Inject
	private Trace trace;

	private TraceTemplate traceTemplate;

	@Before
	public void setUp() {
		traceTemplate = new TraceTemplate(trace);
	}

	@Test
	public void testStartup() throws Exception {
		System.out.println(restTemplate.getInterceptors());

		TraceScope span = trace.startSpan("LARSTEST");
		String response = traceTemplate.trace((traceScope) -> {
			return restTemplate.getForEntity("http://localhost:8080", String.class).getBody();
		});
		System.out.println(response);
		span.close();
	}
}
