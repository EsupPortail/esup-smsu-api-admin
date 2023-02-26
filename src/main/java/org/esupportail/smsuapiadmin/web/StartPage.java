package org.esupportail.smsuapiadmin.web;
 
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.esupportail.smsu.services.UrlGenerator;
import org.esupportail.smsu.web.ServerSideDirectives;
import org.esupportail.smsu.web.ServletContextWrapper;
import javax.inject.Inject;

public class StartPage implements org.springframework.web.HttpRequestHandler {

    @Inject private UrlGenerator urlGenerator;
    @Inject private ServerSideDirectives serverSideDirectives;
    
    private boolean jsonpDisabled = false;

    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
	ServletContextWrapper context = new ServletContextWrapper(request.getSession().getServletContext());
	boolean genTestStaticJsonPage = request.getServletPath().equals("/GenTestStaticJsonPage");
	String baseURL = genTestStaticJsonPage ? ".." : urlGenerator.baseURL(request);
	Map<String, Object> env = createEnv(baseURL, AuthAndRoleAndMiscFilter.getIdpId(request), genTestStaticJsonPage);

	String template = getHtmlTemplate(context, "/WEB-INF/StartPage-template.html");
	String page = instantiateTemplate(context, env, template);
	String type = "text/html; charset=UTF-8";

	response.setContentType(type);
        response.getWriter().print(page);
    }

	public String instantiateTemplate(ServletContextWrapper context, Map<String, Object> env, String template) {
		return serverSideDirectives.instantiate(template, env, context);
	}

	public Map<String, Object> createEnv(String baseURL, String idpId, boolean genTestStaticJsonPage) throws IOException {
		Map<String, Object> env = new TreeMap<>();

    	env.put("baseURL", baseURL);
    	env.put("jsonpDisabled", jsonpDisabled);
    	env.put("useTestStaticJson", genTestStaticJsonPage);
    	if (idpId != null) env.put("idpId", idpId);
    	
    	env.put("globals", new ObjectMapper().writeValueAsString(env));
		return env;
	}

	private <A, B> Map<A, B> singletonMap(A key, B value) {
		Map<A, B> r = new TreeMap<>();
		r.put(key, value);
		return r;
	}

    static public String getHtmlTemplate(ServletContextWrapper context, String path) throws IOException {
	return IOUtils.toString(context.getResourceAsStream(path), "UTF-8");
    }

	public void setJsonpDisabled(boolean jsonpDisabled) {
		this.jsonpDisabled = jsonpDisabled;
	}
}
