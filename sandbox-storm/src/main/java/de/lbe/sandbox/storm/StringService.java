package de.lbe.sandbox.storm;

import org.springframework.stereotype.Component;

@Component
public class StringService {

	public String[] split(String value) {
		return value.split(" ");
	}
}
