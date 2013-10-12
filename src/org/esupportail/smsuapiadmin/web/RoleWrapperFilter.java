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
import org.esupportail.smsuapiadmin.domain.DomainService;
import org.esupportail.smsuapiadmin.domain.beans.EnumeratedFunction;

public final class RoleWrapperFilter implements Filter {
	
    @Autowired
    private DomainService domainService;

    private final Logger logger = new LoggerImpl(getClass());

    public void destroy() {}
    public void init(FilterConfig config) {}

    /**
     * Wraps the HttpServletRequest in a wrapper class that delegates <code>request.isUserInRole</code> 
     */
    public void doFilter(final ServletRequest servletRequest, final ServletResponse response, final FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
	if (request.getRemoteUser() == null) {
	    unauthorized((HttpServletResponse) response);
	    return;
	}

        filterChain.doFilter(new MyHttpServletRequestWrapper(request, retrieveRoles(request)), 
			     response);
    }

    private void unauthorized(HttpServletResponse response) throws IOException {
	response.setHeader("WWW-Authenticate", "CAS");
	response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }

    protected Set<EnumeratedFunction> retrieveRoles(final HttpServletRequest request) {
        String user = request.getRemoteUser();
	return domainService.getUserFunctions(user);
    }

    final class MyHttpServletRequestWrapper extends HttpServletRequestWrapper {

        private final java.util.Set<EnumeratedFunction> roles;

        MyHttpServletRequestWrapper(final HttpServletRequest request, final Set<EnumeratedFunction> roles) {
            super(request);
            this.roles = roles;
        }

        public boolean isUserInRole(final String role) {
	    for (EnumeratedFunction r : roles) {
		if (r.toString().equals(role)) {
		    logger.debug("user has role " + role);
		    return true;
		}
            }
	    String user = getRemoteUser();
	    logger.warn("user " + user + " has not role " + role);
	    for (EnumeratedFunction r : roles) logger.warn("it has role " + r);
	    return false;
        }
    }
}
