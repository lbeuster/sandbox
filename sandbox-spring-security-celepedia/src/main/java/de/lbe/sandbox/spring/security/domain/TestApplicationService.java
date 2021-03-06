package de.lbe.sandbox.spring.security.domain;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class TestApplicationService {

	@PreAuthorize("hasRole('admin')")
	public String service1(String param) {
		return param;
	}

	@PreAuthorize("hasRole('non-existing')")
	public String service2(String param) {
		System.out.println(SecurityContextHolder.getContext().getAuthentication());
		return param;
	}
}