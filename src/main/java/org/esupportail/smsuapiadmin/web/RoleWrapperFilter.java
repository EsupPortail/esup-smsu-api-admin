package org.esupportail.smsuapiadmin.web;

import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.smsuapiadmin.business.UserManager;
import org.esupportail.smsuapiadmin.domain.beans.EnumeratedFunction;

public final class RoleWrapperFilter implements Filter {
	
    @Autowired
    private UserManager userManager;

    private final Logger logger = new LoggerImpl(getClass());

    public void destroy() {}
    public void init(FilterConfig config) {}

    /**
     * Wraps the HttpServletRequest in a wrapper class that delegates <code>request.isUserInRole</code> 
     */
    public void doFilter(final ServletRequest servletRequest, final ServletResponse response, final FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String user = request.getRemoteUser();
	if (user == null) {
	    unauthorized((HttpServletResponse) response);
	    return;
	}

        Set<EnumeratedFunction> roles = userManager.getUserFunctions(user);
        if (request.getHeader("X-Impersonate-User") != null) {
        	user = request.getHeader("X-Impersonate-User");
        	roles = userManager.getUserFunctions(user);
        }
	filterChain.doFilter(new MyHttpServletRequestWrapper(request, user, roles),
			     response);
    }

    private void unauthorized(HttpServletResponse response) throws IOException {
	response.setHeader("WWW-Authenticate", "CAS");
	response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }

    final class MyHttpServletRequestWrapper extends HttpServletRequestWrapper {

        private final java.util.Set<EnumeratedFunction> roles;
        private String user;

        MyHttpServletRequestWrapper(final HttpServletRequest request, String user, final Set<EnumeratedFunction> roles) {
            super(request);
            this.user = user;
            this.roles = roles;
        }

        public String getRemoteUser() {
        	return user;
        }
        
        public boolean isUserInRole(final String role) {
	    for (EnumeratedFunction r : roles) {
		if (r.toString().equals(role)) {
		    logger.debug("user has role " + role);
		    return true;
		}
            }
	    logger.warn("user " + user + " has not role " + role);
	    for (EnumeratedFunction r : roles) logger.warn("it has role " + r);
	    return false;
        }
    }
}
