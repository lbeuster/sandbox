package de.lbe.sandbox.tomcat;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

import org.junit.Test;

import de.asideas.lib.commons.lang.ClassLoaderUtils;
import de.lbe.sandbox.tomcat.testapp.TestBean;

/**
 * @author lars.beuster
 */
public class Jersey2Test extends AbstractJersey2Test {

	@Test
	public void testGET() throws Exception {
		System.out.println(ClassLoaderUtils.getClasspathOfResource("javax/servlet/jsp/resources/jsp_2_1.xsd"));
		String s = prepareClient().request().get(String.class);
		System.out.println(s);
	}

	@Test
	public void testPOST() throws Exception {
		TestBean bean = new TestBean();
		bean.setName("hallo");
		TestBean entity = prepareClient().request().accept(MediaType.APPLICATION_JSON).post(Entity.entity(bean, MediaType.APPLICATION_JSON_TYPE)).readEntity(TestBean.class);
		System.out.println(entity);
		assertEquals(1, entity.getValidationCount());
	}
}
