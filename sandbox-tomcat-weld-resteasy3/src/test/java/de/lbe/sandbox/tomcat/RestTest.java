package de.lbe.sandbox.tomcat;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

import org.junit.Test;

import de.lbe.sandbox.tomcat.testapp.TestBean;
import de.asideas.lib.commons.lang.ClassLoaderUtils;

/**
 * @author lars.beuster
 */
public class RestTest extends AbstractRestTest {

	/**
	 * 
	 */
	@Test
	public void testGET() throws Exception {
		System.out.println(ClassLoaderUtils.getClasspathOfResource("javax/servlet/jsp/resources/jsp_2_1.xsd"));
		TestBean bean = prepareClient().path("rest/myName").request().get(TestBean.class);
		assertEquals("myName", bean.getName());
	}

	/**
	 * 
	 */
	@Test
	public void testInjection() throws Exception {
		TestBean bean = prepareClient().path("rest/testInjection").request().get(TestBean.class);
		assertTrue(bean.isCdiActive());
	}

	/**
	 * 
	 */
	@Test
	public void testContextInjection() throws Exception {
		TestBean bean = prepareClient().path("rest/testContextInjection").request().get(TestBean.class);
		assertTrue(bean.isContextInjectionActive());
	}

	/**
	 * 
	 */
	@Test
	public void testContextInjectionInCdiBean() throws Exception {
		TestBean bean = prepareClient().path("lars/rest/testContextInjectionInCdiBean").request().get(TestBean.class);
		assertTrue(bean.isContextInjectionActive());
	}

	/**
	 * 
	 */
	@Test
	public void testPOST() throws Exception {
		TestBean bean = new TestBean();
		bean.setName("hallo");
		TestBean entity =
			prepareClient().path("rest").request().accept(MediaType.APPLICATION_JSON).post(Entity.entity(bean, MediaType.APPLICATION_JSON_TYPE)).readEntity(TestBean.class);
		System.out.println(entity);
		assertEquals(1, entity.getValidationCount());
	}
}
