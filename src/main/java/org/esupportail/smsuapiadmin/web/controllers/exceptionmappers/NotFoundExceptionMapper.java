package org.esupportail.smsuapiadmin.web.controllers.exceptionmappers;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.esupportail.smsuapiadmin.business.NotFoundException;

@Provider
public class NotFoundExceptionMapper implements
        ExceptionMapper<NotFoundException> {
    @Override
    public Response toResponse(NotFoundException ex) {
        return Helper.jsonErrorResponse(404, ex.getMessage());
    }
}