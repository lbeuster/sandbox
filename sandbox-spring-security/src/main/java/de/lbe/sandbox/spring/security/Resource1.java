package de.lbe.sandbox.spring.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.asideas.ipool.commons.lib.lang.ToString;

/**
 * @author lbeuster
 */
@RestController
@RequestMapping(value = "/path1")
public class Resource1 {

	@RequestMapping("/protected")
	public Object protectedPath() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return ToString.toString(authentication);
	}

	@RequestMapping("/public")
	public Object publicPath() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		System.out.println(ToString.toString(authentication));
		return "success";
	}
}
