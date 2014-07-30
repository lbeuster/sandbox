package de.lbe.sandbox.spring.security;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import de.asideas.lib.commons.spring.security.CustomGlobalMethodSecurityConfiguration;

@Configuration
@ComponentScan("de.lbe.sandbox")
public class SecurityConfig extends CustomGlobalMethodSecurityConfiguration {
}
