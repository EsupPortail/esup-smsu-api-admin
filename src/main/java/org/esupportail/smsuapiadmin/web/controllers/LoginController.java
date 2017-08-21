package org.esupportail.smsuapiadmin.web.controllers;

import java.io.IOException;
import java.net.URI;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import javax.inject.Inject;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.codehaus.jackson.map.ObjectMapper;
import org.esupportail.smsuapiadmin.business.UserManager;
import org.esupportail.smsuapiadmin.dto.beans.UIUser;
import org.esupportail.smsuapiadmin.web.AuthAndRoleAndMiscFilter;
import org.esupportail.smsu.services.UrlGenerator;

@Path("/login")
public class LoginController {
	
    @Inject private UserManager userManager;
    @Inject private UrlGenerator urlGenerator;
    
    @GET
    public Response get(@Context HttpServletRequest request) throws IOException {
    	boolean ourCookiesRejected = !hasCookie(request, "JSESSIONID");

    HttpSession session = request.getSession();
	String sessionId = ourCookiesRejected ? session.getId() : null;
	String idpId = AuthAndRoleAndMiscFilter.getIdpId(request);
	String then = request.getParameter("then");
	if (then != null) {
		//then = URLDecoder.decode(then, "UTF-8");
		String url = urlGenerator.goTo(request, then, sessionId, idpId);
		return Response.temporaryRedirect(URI.create(url)).build();		
	}

	UIUser user = userManager.getUserByLogin(request.getRemoteUser());
	user.idpId = idpId;
	if (ourCookiesRejected) {
		user.sessionId = session.getId();
	}
	String jsUser = new ObjectMapper().writeValueAsString(user);
	String content, type;
	if (request.getParameter("postMessage") != null) {
		type = "text/html";
		content = "Login success, please wait...\n<script>\n (window.opener ? (window.opener.postMessage ? window.opener : window.opener.document) : window.parent).postMessage('loggedUser=' + JSON.stringify(" + jsUser + "), '*');\n</script>";
	} else if (request.getParameter("callback") != null) {
		type = "application/x-javascript";
		content = request.getParameter("callback") + "(" + jsUser + ")";
	} else {
		type = "application/json";
		content = jsUser;
	}
        return Response.status(Response.Status.OK).type(type).entity(content).build();
    }

	// call this function on successful login
    // if we managed to get here and there is no cookie, it means they have been rejected
    public static boolean hasCookie(HttpServletRequest request, String name) {
    	for (Cookie cookie : request.getCookies()) {
    		if (cookie.getName().equals(name))
    			// cool, our previous Set-Cookie was accepted
    			return true;
    	}
    	return false;
    }

}