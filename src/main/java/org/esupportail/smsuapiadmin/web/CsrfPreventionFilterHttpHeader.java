/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.esupportail.smsuapiadmin.web;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.Random;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;

/**
 * Provides basic CSRF protection for a web application. 
 * The filter assumes that accesses from client have a HTTP header X-XSRF-TOKEN with value from cookie XSRF-TOKEN
 * (as done for example by AngularJS).
 * 
 * With a short cookieMaxAge, it is the responsability of the client to resend non-GET requests with new token
 *
 * inspired from org.apache.catalina.filters.CsrfPreventionFilter
 */
public class CsrfPreventionFilterHttpHeader implements Filter {

    @SuppressWarnings("unused")
    private final Logger logger = new LoggerImpl(getClass());
    private Random randomSource = new SecureRandom();

    private int cookieMaxAge = 10 * 60; // 10 minutes
    private int denyStatus = HttpServletResponse.SC_FORBIDDEN;
    private String cookieName = "XSRF-TOKEN";
    private String httpHeaderName = "X-XSRF-TOKEN";
    private String sessionAttrName = "org.esupportail.smsuapiadmin.web.CSRF_TOKEN";
    private String jsonErrorMessage = "{ \"error\": \"Invalid CRSF prevention token\" }";
    
    public void destroy() {}
    public void init(FilterConfig config) {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {

        if (request instanceof HttpServletRequest &&
                response instanceof HttpServletResponse) {

            HttpServletRequest req = (HttpServletRequest) request;
            HttpServletResponse res = (HttpServletResponse) response;

            HttpSession session = req.getSession(false);

            String existingToken = (session == null) ? null
		: (String) session.getAttribute(sessionAttrName);

            boolean skipTokenCheck = "GET".equals(req.getMethod());
            boolean deny = false;
            if (!skipTokenCheck) {
                String previousToken = req.getHeader(httpHeaderName);

                deny = existingToken == null || previousToken == null ||
                        !existingToken.equals(previousToken);
            }

            if (existingToken == null || deny) {
                if (session == null) {
                    session = req.getSession(true);
                }
		String newToken = generateToken();
                session.setAttribute(sessionAttrName, newToken);
		setCookie(req, res, newToken);
            }
            
            if (deny) {
                res.setStatus(denyStatus);
                res.setContentType("application/json");
                res.getOutputStream().println(jsonErrorMessage);
                return;
            }
        }
        chain.doFilter(request, response);
    }

    private void setCookie(HttpServletRequest req, HttpServletResponse res, String newToken) {
	Cookie c = new Cookie(cookieName, newToken);
	String path = req.getContextPath();
	if (path.equals("")) path = "/";
	c.setPath(path);
	c.setMaxAge(cookieMaxAge);
	res.addCookie(c);
    }

    /**
     * Generate a token for authenticating subsequent
     * requests. This will also add the token to the session. The token
     * generation is a simplified version of ManagerBase.generateSessionId().
     *
     */
    protected String generateToken() {
        byte random[] = new byte[16];

        // Render the result as a String of hexadecimal digits
        StringBuilder buffer = new StringBuilder();

        randomSource.nextBytes(random);

        for (int j = 0; j < random.length; j++) {
            byte b1 = (byte) ((random[j] & 0xf0) >> 4);
            byte b2 = (byte) (random[j] & 0x0f);
            if (b1 < 10) {
                buffer.append((char) ('0' + b1));
            } else {
                buffer.append((char) ('A' + (b1 - 10)));
            }
            if (b2 < 10) {
                buffer.append((char) ('0' + b2));
            } else {
                buffer.append((char) ('A' + (b2 - 10)));
            }
        }

        return buffer.toString();
    }
}
