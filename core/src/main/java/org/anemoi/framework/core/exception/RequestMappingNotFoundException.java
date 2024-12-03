package org.anemoi.framework.core.exception;

public class RequestMappingNotFoundException extends IllegalArgumentException {
    String message ;
    public RequestMappingNotFoundException(String message){
        super(message);
    }

}
