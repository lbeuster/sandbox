/*
 * Copyright 2012-2014 the original author or authors.
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

package de.lbe.sandbox.springboot.jersey1;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.spi.container.WebApplication;
import com.sun.jersey.spi.container.servlet.ServletContainer;
import com.sun.jersey.spi.spring.container.SpringComponentProviderFactory;

/**
 * @author lbeuster
 */
@Configuration
@ConditionalOnWebApplication
@Order(Ordered.HIGHEST_PRECEDENCE)
public class Jersey1AutoConfiguration {

	@Bean
	public FilterRegistrationBean jersey(final ConfigurableApplicationContext context) {
		FilterRegistrationBean bean = new FilterRegistrationBean();
		bean.addUrlPatterns("/api/*");
		bean.setFilter(new ServletContainer() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void initiate(ResourceConfig rc, WebApplication wa) {
				wa.initiate(rc, new SpringComponentProviderFactory(rc, context));
			}
		});
		bean.addInitParameter("com.sun.jersey.config.property.packages", "com.sun.jersey;" + getClass().getPackage().getName());
		// bean.addInitParameter("javax.ws.rs.Application", HelloApplication.class.getName());
		return bean;
	}

}
