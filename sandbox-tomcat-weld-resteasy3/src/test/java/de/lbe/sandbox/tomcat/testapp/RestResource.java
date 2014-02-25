package de.lbe.sandbox.tomcat.testapp;

import java.util.Enumeration;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.lang3.SystemUtils;

/**
 * @author lars.beuster
 */
@Path("/rest")
@ApplicationScoped
public class RestResource {

	@Inject
	BeanManager beanManager;

	@Context
	private UriInfo uriInfo;

	@Context
	private HttpServletRequest httpRequest;

	@Inject
	private TestService service;

	public RestResource() {
		System.out.println(getClass().getName() + "#<init>");
	}

	@GET
	@Produces("application/json")
	@Path("{name}")
	public TestBean validate(@PathParam("name") String name) {
		TestBean bean = new TestBean();
		bean.setName(name);
		return bean;
	}

	@GET
	@Produces("application/json")
	@Path("/testInjection")
	public TestBean testInjection() {
		TestBean bean = new TestBean();
		bean.setCdiActive(this.beanManager != null);
		return bean;
	}

	@GET
	@Produces("application/json")
	@Path("/testContextInjection")
	public TestBean testContextInjection() {
		TestBean bean = new TestBean();
		bean.setContextInjectionActive(this.uriInfo != null);
		service.service(null);
		return bean;
	}

	@GET
	@Produces("application/json")
	@Path("/testContextInjectionInCdiBean")
	public TestBean testContextInjectionInCdiBean() {
		System.out.println(toString(this.httpRequest, true, true, true));
		TestBean bean = new TestBean();
		bean.setContextInjectionActive(this.service.uriInfo() != null);
		return bean;
	}

	@POST
	@Produces("application/json")
	public TestBean validate(@Valid TestBean testBean) {
		return testBean;

		// TestService1 service = BeanManagerUtils.getContextualReference(CDI.current().getBeanManager(), TestService1.class);
		// return service.service(testBean);
	}

	/***************************************************************************
     *
     **************************************************************************/

	@SuppressWarnings("unchecked")
	// @unchecked-servlet-api
	public static final String toString(HttpServletRequest request, boolean dump_headers, boolean dump_params, boolean dump_attributes) {

		if (request == null)
			return null;

		StringBuilder sb = new StringBuilder(1000).append(SystemUtils.LINE_SEPARATOR);

		// URL
		sb.append("URL:").append(SystemUtils.LINE_SEPARATOR).append("\trequestURI: ").append(request.getRequestURI()).append(SystemUtils.LINE_SEPARATOR).append("\trequestURL: ")
			.append(request.getRequestURL()).append(SystemUtils.LINE_SEPARATOR).append("\tqueryString: ").append(request.getQueryString()).append(SystemUtils.LINE_SEPARATOR)
			.append("\tservletPath: ")
			.append(request.getServletPath())
			.append(SystemUtils.LINE_SEPARATOR)
			.append("\tpathInfo: ")
			.append(request.getPathInfo())
			.append(SystemUtils.LINE_SEPARATOR)
			.append(SystemUtils.LINE_SEPARATOR)

			// Server
			.append("Server:").append(SystemUtils.LINE_SEPARATOR).append("\tscheme (isSecure): ").append(request.getScheme()).append(" (").append(request.isSecure()).append(")")
			.append(SystemUtils.LINE_SEPARATOR).append("\tserverName: ").append(request.getServerName()).append(SystemUtils.LINE_SEPARATOR)
			.append("\tserverPort: ")
			.append(request.getServerPort())
			.append(SystemUtils.LINE_SEPARATOR)
			.append(SystemUtils.LINE_SEPARATOR)

			// RemoteParams
			.append("Remote:").append(SystemUtils.LINE_SEPARATOR).append("\tremoteAddr: ").append(request.getRemoteAddr()).append(SystemUtils.LINE_SEPARATOR)
			.append("\tremoteHost:remotePort ").append(request.getRemoteHost()).append(":").append(request.getRemotePort()).append(SystemUtils.LINE_SEPARATOR)
			.append("\tremoteUser: ").append(request.getRemoteUser()).append(SystemUtils.LINE_SEPARATOR)
			.append(SystemUtils.LINE_SEPARATOR)

			// LocalParams
			.append("Local:").append(SystemUtils.LINE_SEPARATOR).append("\tlocalAddr:localPort ").append(request.getLocalAddr()).append(":").append(request.getLocalPort())
			.append(SystemUtils.LINE_SEPARATOR).append("\tlocalName: ").append(request.getLocalName()).append(SystemUtils.LINE_SEPARATOR).append(SystemUtils.LINE_SEPARATOR)

			// Method
			.append("Method: ").append(request.getMethod()).append(SystemUtils.LINE_SEPARATOR).append("UserPrincipal: ").append(request.getUserPrincipal())
			.append(SystemUtils.LINE_SEPARATOR).append(SystemUtils.LINE_SEPARATOR);

		// Headers
		if (dump_headers) {
			sb.append("Headers:");
			for (Enumeration<String> names = request.getHeaderNames(); names.hasMoreElements();) {
				String name = names.nextElement();
				String value = request.getHeader(name);
				sb.append(SystemUtils.LINE_SEPARATOR).append("\t").append(name).append("=").append(value);
			}
			sb.append(SystemUtils.LINE_SEPARATOR);
			sb.append(SystemUtils.LINE_SEPARATOR);
		}

		// Parameters
		if (dump_params) {
			sb.append("Parameters:");
			for (Enumeration<String> params = request.getParameterNames(); params.hasMoreElements();) {
				String name = params.nextElement();
				String value = request.getParameter(name);
				sb.append(SystemUtils.LINE_SEPARATOR).append("\t").append(name).append(": <").append(value).append(">");
			}
			sb.append(SystemUtils.LINE_SEPARATOR);
			sb.append(SystemUtils.LINE_SEPARATOR);
		}

		// Attribute
		if (dump_attributes) {
			sb.append("Attributes:");
			for (Enumeration<String> attrs = request.getAttributeNames(); attrs.hasMoreElements();) {
				String name = attrs.nextElement();
				Object value = request.getAttribute(name);
				sb.append(SystemUtils.LINE_SEPARATOR).append("\t").append(name).append(": <").append(value).append(">");
			}
			sb.append(SystemUtils.LINE_SEPARATOR);
			sb.append(SystemUtils.LINE_SEPARATOR);
		}
		return sb.toString();

	} // HttpRequestHelper.toString()

}
