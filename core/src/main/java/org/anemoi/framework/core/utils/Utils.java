package org.anemoi.framework.core.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import org.anemoi.framework.core.mapping.Controller;

import com.google.common.reflect.ClassPath;
import org.anemoi.framework.core.route.RouteRegistry;

import java.io.IOException;

public class Utils {

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

   public static RouteRegistry urlMethodClassRegistry(List<Class<?>> clazz){
       HashMap<String, Method> getRoute = new HashMap<>();
       HashMap<String,Method> postRoute = new HashMap<>();
       for (Class<?> c : clazz){

       }

       return null;
   }


   public static   Map<String,Method> getSpecifiedHttpRequestTypeRoute(Class<?> clazz, Class<? extends Annotation> httpRequestTypeClass) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
       HashMap<String, Method> routes = new HashMap<>();
       Method[] clazzMethods = clazz.getDeclaredMethods();
       Controller controller = clazz.getAnnotation(Controller.class);
       for (Method m  : clazzMethods){
           if(m.isAnnotationPresent(httpRequestTypeClass)) {
               Annotation annotation =m.getAnnotation(httpRequestTypeClass);
               String finalMethodUrl = completeRequestURL(controller.baseUrl(),annotation.annotationType().getMethod("value").invoke(annotation).toString());
               System.out.println(finalMethodUrl);
               routes.put(finalMethodUrl,m);
           }

       }
       return routes;
   }

   private  static String completeRequestURL(String controllerBaseUrl,String methodUrl){
        return new StringBuilder()
                .append(controllerBaseUrl).append(methodUrl)
                .toString();
   }
}
