package de.lbe.sandbox.springboot.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author lbeuster
 */
@Component
@ConfigurationProperties(prefix = "my", ignoreUnknownFields = false)
public class MyConfig {

	private String override;

	private String def;

	private Features features;

	public static class Features {

		private String feature1;

		public String getFeature1() {
			return feature1;
		}

		public void setFeature1(String feature1) {
			this.feature1 = feature1;
		}
	}

	public String getOverride() {
		return override;
	}

	public void setOverride(String override) {
		this.override = override;
	}

	public String getDef() {
		return def;
	}

	public void setDef(String def) {
		this.def = def;
	}

	public Features getFeatures() {
		return features;
	}

	public void setFeatures(Features features) {
		this.features = features;
	}
}