package org.anemoi.framework.core.route;

import lombok.AllArgsConstructor;
import lombok.Builder;

import java.lang.reflect.Method;
import java.util.Map;


@Builder
public class RouteRegistry {
    private Map<String, Method> getRoute;
    private Map<String,Method> postRoute;
    private Map<String,Method> putRoute;
    private Map<String,Method> deleteRoute;
}
