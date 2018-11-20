package org.esupportail.smsuapiadmin.web.controllers.exceptionmappers;

import org.esupportail.smsuapiadmin.web.controllers.InvalidParameterException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class InvalidParameterExceptionMapper {
    @ExceptionHandler(InvalidParameterException.class)
    public ResponseEntity<?> toResponse(Exception ex) {
        return Helper.jsonErrorResponse(HttpStatus.BAD_REQUEST, ex.toString());
    }
}