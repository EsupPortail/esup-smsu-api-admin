package org.esupportail.smsuapiadmin.web.controllers;

import java.io.IOException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jackson.map.ObjectMapper;
import org.esupportail.smsuapiadmin.business.UserManager;
import org.esupportail.smsuapiadmin.dto.beans.UIUser;

@Path("/login")
public class LoginController {
	
    @Autowired
    private UserManager userManager;

    @GET
    public Response get(@Context HttpServletRequest request) throws IOException {
	UIUser user = userManager.getUserByLogin(request.getRemoteUser());
	String jsUser = new ObjectMapper().writeValueAsString(user);
	String callback = request.getParameter("callback");
	String type = callback == null ? "text/html" : "application/x-javascript";
	String js = 
	    callback == null ?
	    "Login success, please wait...\n<script>\n (window.opener.postMessage ? window.opener : window.opener.document).postMessage('loggedUser=' + JSON.stringify(" + jsUser + "), '*');\n</script>" :
	    callback + "(" + jsUser + ")";
	
        return Response.status(Response.Status.OK).type(type).entity(js).build();
    }
}