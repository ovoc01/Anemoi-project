package org.anemoi.framework.core.route;


import lombok.Builder;

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
        return extractHttpMethodRouteMap(httpMethod).get(requestMappingUrl);
    }
}
