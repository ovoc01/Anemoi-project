package org.anemoi.framework.core;

public class RequestMappingNotFoundException extends IllegalArgumentException {
    String message ;
    public RequestMappingNotFoundException(String message){
        super(message);
    }

}
