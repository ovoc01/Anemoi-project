package org.anemoi.framework.core.route;


import lombok.Builder;
import org.anemoi.framework.core.exception.RequestMappingNotFoundException;

import java.lang.reflect.Method;
import java.util.Map;


@Builder
public class RouteRegistry {
    private Map<String, Method> getRoute;
    private Map<String,Method> postRoute;
    private Map<String,Method> putRoute;
    private Map<String,Method> deleteRoute;

    private Map<String, Method> extractHttpMethodRouteMap(String httpMethod) {
        return switch (httpMethod) {
            case "GET" -> getRoute;
            case "POST" -> postRoute;
            case "PUT" -> putRoute;
            case "DELETE" -> deleteRoute;
            default -> null;
        };
    }

    public Method extractMethodFromRoute(String httpMethod,String requestMappingUrl){
        Method requiredMethod = extractHttpMethodRouteMap(httpMethod).get(requestMappingUrl);
        if(requiredMethod == null) throw new RequestMappingNotFoundException(String.format("No mapping found for %s with %s HttpMethod",requestMappingUrl,httpMethod));
        return requiredMethod;
    }
}
