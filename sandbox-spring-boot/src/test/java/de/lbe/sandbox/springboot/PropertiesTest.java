package de.lbe.sandbox.springboot;

import javax.inject.Inject;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ActiveProfiles;

import de.lbe.sandbox.springboot.conf.MyConfig;

/**
 * @author lbeuster
 */
@ActiveProfiles("test")
public class PropertiesTest extends AbstractSpringBootTest {

	@Value("${my.override}")
	private String overriddenProperty;

	@Value("${my.def}")
	private String defaultProperty;

	@Inject
	private MyConfig myConfig;

	@Test
	public void testOverrideProperty() throws Exception {
		System.out.println("overriddenProperty=" + this.overriddenProperty);
		assertNotNull(this.overriddenProperty);
		assertEquals("application-test.properties", this.overriddenProperty);
	}

	@Test
	public void testDefaultProperty() throws Exception {
		System.out.println("defaultProperty=" + this.defaultProperty);
		assertNotNull(this.defaultProperty);
		assertEquals("application.properties", this.defaultProperty);
	}

	@Test
	public void testTypesafeConfig() {
		assertEquals("application-test.properties", myConfig.getOverride());
		assertEquals("application.properties", myConfig.getDef());
		assertEquals("FEATURE1", myConfig.getFeatures().getFeature1());

	}
}