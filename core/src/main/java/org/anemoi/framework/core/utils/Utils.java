package org.anemoi.framework.core.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.anemoi.framework.Main;
import org.anemoi.framework.core.mapping.Controller;

import com.google.common.reflect.ClassPath;
import org.anemoi.framework.core.mapping.binding.DeleteMapping;
import org.anemoi.framework.core.mapping.binding.GetMapping;
import org.anemoi.framework.core.mapping.binding.PostMapping;
import org.anemoi.framework.core.mapping.binding.PutMapping;
import org.anemoi.framework.core.route.RouteRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Utils {

    private static final Logger logger = LoggerFactory.getLogger(Utils.class);


    public static List<Class<?>> retrieveAllApplicationControllerClass(String packageName) throws IOException {
        return ClassPath.from(ClassLoader.getSystemClassLoader())
                .getAllClasses()
                .stream()
                .filter(classInfo -> classInfo.getPackageName().contains(packageName))
                .filter(classInfo -> classInfo.load().isAnnotationPresent(Controller.class))
                .map(classInfo -> classInfo.load())
                .collect(Collectors.toList())
                ;
    }

    public static RouteRegistry urlMethodClassRegistry(List<Class<?>> clazz) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        HashMap<String, Method> getRoute = new HashMap<>();
        HashMap<String, Method> postRoute = new HashMap<>();
        HashMap<String, Method> putRoute = new HashMap<>();
        HashMap<String, Method> deleteRoute = new HashMap<>();
        for (Class<?> c : clazz) {
            Map<String,Method> getRouteMethods = getSpecifiedHttpRequestTypeRoute(c, GetMapping.class);
            Map<String,Method> postRouteMethods = getSpecifiedHttpRequestTypeRoute(c, PostMapping.class);
            Map<String,Method> putRouteMethods = getSpecifiedHttpRequestTypeRoute(c, PutMapping.class);
            Map<String,Method> deleteRouteMethods = getSpecifiedHttpRequestTypeRoute(c, DeleteMapping.class);

            getRoute.putAll(getRouteMethods);
            postRoute.putAll(postRouteMethods);
            putRoute.putAll(putRouteMethods);
            deleteRoute.putAll(deleteRouteMethods);
        }


        return RouteRegistry.builder()
                .getRoute(getRoute)
                .postRoute(postRoute)
                .deleteRoute(deleteRoute)
                .putRoute(putRoute)
                .build();
    }


    public static Map<String, Method> getSpecifiedHttpRequestTypeRoute(Class<?> clazz, Class<? extends Annotation> httpRequestTypeClass) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        logger.info("Scanning the application to find method annoted with @{} ...",httpRequestTypeClass.getCanonicalName());
        HashMap<String, Method> routes = new HashMap<>();
        Method[] clazzMethods = clazz.getDeclaredMethods();
        Controller controller = clazz.getAnnotation(Controller.class);
        for (Method m : clazzMethods) {
            if (m.isAnnotationPresent(httpRequestTypeClass)) {
                Annotation annotation = m.getAnnotation(httpRequestTypeClass);
                String finalMethodUrl = completeRequestURL(controller.baseUrl(), annotation.annotationType().getMethod("value").invoke(annotation).toString());
                routes.put(finalMethodUrl, m);
                logger.info("{} {} added to the RouteRegistry",finalMethodUrl,m);
            }
        }
        logger.info("Application scan finished");
        return routes;
    }

    private static String completeRequestURL(String controllerBaseUrl, String methodUrl) {
        return new StringBuilder()
                .append(controllerBaseUrl).append(methodUrl)
                .toString();
    }


}
