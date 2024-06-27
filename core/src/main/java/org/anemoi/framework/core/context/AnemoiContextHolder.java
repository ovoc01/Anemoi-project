package org.anemoi.framework.core.context;

import lombok.Getter;
import org.anemoi.framework.core.route.RouteRegistry;
import org.anemoi.framework.core.utils.Utils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;


public final class AnemoiContextHolder {
    private static AnemoiContextHolder anemoiContextHolder;

    @Getter
    private RouteRegistry routeRegistry;
    private HashMap<Class<?>, Object> classObjectInstanceHashMap;

    public static AnemoiContextHolder getInstance(){
        if(anemoiContextHolder==null){
            anemoiContextHolder = new AnemoiContextHolder();
            return anemoiContextHolder;
        }
        return anemoiContextHolder;
    }



    public void registerRoute(String basePackageName) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, IOException {
        List<Class<?>> classList = Utils.retrieveAllApplicationControllerClass(basePackageName);
        this.routeRegistry = Utils.urlMethodClassRegistry(classList);
    }

}
