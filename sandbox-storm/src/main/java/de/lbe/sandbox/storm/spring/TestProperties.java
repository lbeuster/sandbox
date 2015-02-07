package de.lbe.sandbox.storm.spring;

import org.springframework.boot.context.properties.ConfigurationProperties;

import de.asideas.ipool.commons.lib.lang.ToString;

@ConfigurationProperties(prefix = "mongo", ignoreUnknownFields = false)
public class TestProperties {
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return ToString.toString(this);
	}

}
