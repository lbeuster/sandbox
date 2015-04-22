package de.lbe.sandbox.spring.security.domain;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({ SessionRepository.class, AuthenticationService.class, TestApplicationService.class, UserRepository.class })
public class MyDomainConfiguration {

}