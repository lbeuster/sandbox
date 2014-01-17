package de.lbe.sandbox.aws;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.cloud.Cloud;
import org.springframework.cloud.CloudFactory;
import org.springframework.cloud.cloudfoundry.CloudFoundryConnector;
import org.springframework.cloud.heroku.HerokuConnector;
import org.springframework.cloud.service.BaseServiceInfo;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.cloud.util.EnvironmentAccessor;

import de.asideas.lib.commons.lang.reflect.ReflectionUtils;
import de.asideas.lib.commons.test.junit.AbstractJUnit4Test;

/**
 * @author lbeuster
 */
public class SpringCloudTest extends AbstractJUnit4Test {

	/**
	 * 
	 */
	@Test
	public void testCloud() {
		CloudFactory cloudFactory = new CloudFactory();
		Cloud cloud = cloudFactory.getCloud();
		List<ServiceInfo> serviceInfos = cloud.getServiceInfos();
		System.out.println(serviceInfos);
	}

	/**
	 * Only Postgres is currently supported on Heroku.
	 */
	@Test
	public void testHeroku() throws Exception {
		EnvironmentAccessor env = new TestEnvironmentAccessor();

		HerokuConnector cloud = new HerokuConnector();
		ReflectionUtils.invokeInstanceMethod(cloud, "setCloudEnvironment", true, new Class[] { EnvironmentAccessor.class }, new Object[] { env });
		List<ServiceInfo> serviceInfos = cloud.getServiceInfos();
		System.out.println(serviceInfos);
	}

	/**
	 * Even in CF there have to be defined names in the VCAP. All services in out example are not recognized as what they are: Mongo, Rabbit, MySQL.
	 */
	@Test
	public void testCloudFoundry() throws Exception {

		String vcapServices =
			"{\"user-provided\":[{\"name\":\"newrelic-b07be\",\"label\":\"user-provided\",\"tags\":[],\"credentials\":"
				+ "{\"licenseKey\":\"3a464e833a3bac85b3535cd038236abff72697f4\"}},{\"name\":\"upsi-mongo\",\"label\":\"user-provided\",\"tags\":[],"
				+ "\"credentials\":{\"uri\":\"mongodb://celepedia:c5f0f00ee1b145@151.249.119.68:27017/celepedia\"}},{\"name\":\"upsi-amqp\",\"label\":\"user-provided\","
				+ "\"tags\":[],\"credentials\":{\"uri\":\"amqp://xzviweay:4GVHlR5hIntWcrRHgxQvXLSW_6KwCCDW@bunny.cloudamqp.com/xzviweay\"}}]}";

		EnvironmentAccessor env = new TestEnvironmentAccessor();
		env.getEnv().put("VCAP_SERVICES", vcapServices);
		CloudFoundryConnector cloud = new CloudFoundryConnector();
		ReflectionUtils.invokeInstanceMethod(cloud, "setCloudEnvironment", true, new Class[] { EnvironmentAccessor.class }, new Object[] { env });

		List<ServiceInfo> serviceInfos = cloud.getServiceInfos();
		for (ServiceInfo info : serviceInfos) {
			BaseServiceInfo baseInfo = (BaseServiceInfo) info;
			System.out.println(info.getId() + ": " + info);
		}
		System.out.println(serviceInfos);
	}

	/**
	 * 
	 */
	private static class TestEnvironmentAccessor extends EnvironmentAccessor {

		private final Map<String, String> localEnv = new HashMap<>(System.getenv());

		@Override
		public Map<String, String> getEnv() {
			return this.localEnv;
		}

		@Override
		public String getEnvValue(String key) {
			return getEnv().get(key);
		}

	}
}