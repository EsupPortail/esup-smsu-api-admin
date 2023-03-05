package org.esupportail.smsuapiadmin.web;
 
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.esupportail.smsu.web.ServletContextWrapper;
import javax.inject.Inject;

public class StartPage implements org.springframework.web.HttpRequestHandler {

    private boolean jsonpDisabled = false;

    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
	ServletContextWrapper context = new ServletContextWrapper(request.getSession().getServletContext());
	boolean genTestStaticJsonPage = request.getServletPath().equals("/GenTestStaticJsonPage");
	String globals = createGlobals(AuthAndRoleAndMiscFilter.getIdpId(request), genTestStaticJsonPage);

	String template = getHtmlTemplate(context, "/index.html");
	String page = template.replaceFirst("globals = .*", "globals = " + globals);
	String type = "text/html; charset=UTF-8";

	response.setContentType(type);
        response.getWriter().print(page);
    }

	public String createGlobals(String idpId, boolean genTestStaticJsonPage) throws IOException {
		Map<String, Object> env = new TreeMap<>();

    	env.put("baseURL", ".");
    	env.put("jsonpDisabled", jsonpDisabled);
    	if (idpId != null) env.put("idpId", idpId);
        if (genTestStaticJsonPage) env.put("test", true);
    	
    	return new ObjectMapper().writeValueAsString(env);
	}

    static public String getHtmlTemplate(ServletContextWrapper context, String path) throws IOException {
	return IOUtils.toString(context.getResourceAsStream(path), "UTF-8");
    }

	public void setJsonpDisabled(boolean jsonpDisabled) {
		this.jsonpDisabled = jsonpDisabled;
	}
}
