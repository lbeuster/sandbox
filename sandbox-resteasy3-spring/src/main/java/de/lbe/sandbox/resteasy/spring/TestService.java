package de.lbe.sandbox.resteasy.spring;

import javax.ws.rs.core.Application;

import org.springframework.stereotype.Component;

@Component
public class TestService extends Application {

	public String service(String param) {
		return param;
	}
}