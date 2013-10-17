package org.esupportail.smsuapiadmin.web.controllers.exceptionmappers;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.springframework.dao.DataIntegrityViolationException;

@Provider
public class DataIntegrityViolationExceptionMapper implements
        ExceptionMapper<DataIntegrityViolationException> {
    @Override
    public Response toResponse(DataIntegrityViolationException ex) {
        return Helper.jsonErrorResponse(400, ex.getMostSpecificCause().getMessage());
    }
}