package com.microservices.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class UnsuccessfulAuthorizationException extends RuntimeException{
    private static final long serialVersionUID = 2136472234910389713L;
    private String resourceName;
    private String fieldName;
    private Object fieldValue;

    public UnsuccessfulAuthorizationException( String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s not authorized with %s : '%s'", resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public String getResourceName() {
        return resourceName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Object getFieldValue() {
        return fieldValue;
    }
}