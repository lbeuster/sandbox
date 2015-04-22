package de.lbe.sandbox.spring.security.rest;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({ TestApplication.class, MainResource.class })
public class MyRestConfiguration {

}