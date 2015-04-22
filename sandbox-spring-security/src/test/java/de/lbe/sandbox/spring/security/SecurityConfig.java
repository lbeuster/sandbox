package de.lbe.sandbox.spring.security;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import de.asideas.ipool.commons.lib.spring.security.CustomGlobalMethodSecurityConfiguration;

@Configuration
@ComponentScan("de.lbe.sandbox")
public class SecurityConfig extends CustomGlobalMethodSecurityConfiguration {
}
