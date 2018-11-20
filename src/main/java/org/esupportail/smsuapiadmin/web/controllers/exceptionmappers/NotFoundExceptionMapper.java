package org.esupportail.smsuapiadmin.web.controllers.exceptionmappers;

import org.esupportail.smsuapiadmin.business.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class NotFoundExceptionMapper {
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> toResponse(Exception ex) {
        return Helper.jsonErrorResponse(HttpStatus.NOT_FOUND, ex.toString());
    }
}
