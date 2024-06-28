package org.anemoi.framework.core.context;

import lombok.Getter;
import org.anemoi.framework.core.route.RouteRegistry;
import org.anemoi.framework.core.utils.Utils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;


public final class AnemoiContext {


    @Getter
    private RouteRegistry routeRegistry;
    private HashMap<Class<?>, Object> beans;


    private void initializeBeansComponent(){

    }

    public <T> T getBean(Class<T> clazz) {
        return clazz.cast(beans.get(clazz));
    }

    public void registerRoute(String basePackageName) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, IOException {
        List<Class<?>> classList = Utils.retrieveAllApplicationControllerClass(basePackageName);
        this.routeRegistry = Utils.urlMethodClassRegistry(classList);
    }

}
