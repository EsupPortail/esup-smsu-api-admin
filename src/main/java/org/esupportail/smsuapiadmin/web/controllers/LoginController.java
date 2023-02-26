package org.esupportail.smsuapiadmin.web.controllers;

import java.io.IOException;
import java.net.URI;

import javax.inject.Inject;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.codehaus.jackson.map.ObjectMapper;
import org.esupportail.smsuapiadmin.business.UserManager;
import org.esupportail.smsuapiadmin.dto.beans.UIUser;
import org.esupportail.smsuapiadmin.web.AuthAndRoleAndMiscFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.esupportail.smsu.services.UrlGenerator;

@RestController
@RequestMapping(value = "/login")
public class LoginController {
	
    @Inject private UserManager userManager;
    @Inject private UrlGenerator urlGenerator;
    
	@RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<String> get(HttpServletRequest request) throws IOException {

	    HttpSession session = request.getSession();
		String idpId = AuthAndRoleAndMiscFilter.getIdpId(request);
		String then = request.getParameter("then");
		if (then != null) {
			//then = URLDecoder.decode(then, "UTF-8");
			String url = urlGenerator.goTo(request, then, idpId);
			HttpHeaders responseHeaders = new HttpHeaders();
			responseHeaders.setLocation(URI.create(url));
			return new ResponseEntity<String>("", responseHeaders, HttpStatus.TEMPORARY_REDIRECT);
		}
	
		UIUser user = userManager.getUserByLogin(request.getRemoteUser());
		user.idpId = idpId;
		String jsUser = new ObjectMapper().writeValueAsString(user);
		String content;
		MediaType mediaType;
		if (request.getParameter("postMessage") != null) {
			mediaType = MediaType.TEXT_HTML;
			content = "Login success, please wait...\n<script>\n (window.opener ? (window.opener.postMessage ? window.opener : window.opener.document) : window.parent).postMessage('loggedUser=' + JSON.stringify(" + jsUser + "), '*');\n</script>";
		} else if (request.getParameter("callback") != null) {
			mediaType = MediaType.parseMediaType("application/x-javascript");
			content = request.getParameter("callback") + "(" + jsUser + ")";
		} else {
			mediaType = MediaType.APPLICATION_JSON;
			content = jsUser;
		}
        HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.setContentType(mediaType);
        return new ResponseEntity<String>(content, responseHeaders, HttpStatus.OK);
    }

	// call this function on successful login
    // if we managed to get here and there is no cookie, it means they have been rejected
    private boolean hasCookie(HttpServletRequest request, String name) {
    	for (Cookie cookie : request.getCookies()) {
    		if (cookie.getName().equals(name))
    			// cool, our previous Set-Cookie was accepted
    			return true;
    	}
    	return false;
    }

}