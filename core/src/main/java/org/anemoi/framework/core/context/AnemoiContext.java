package org.anemoi.framework.core.context;

import lombok.Getter;
import org.anemoi.framework.core.route.RouteRegistry;
import org.anemoi.framework.core.utils.Utils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public final class AnemoiContext {

    @Getter
    private RouteRegistry routeRegistry;
    private Map<Class<?>, Object> beans = new ConcurrentHashMap<>();


    public AnemoiContext(){
        initializeRequiredBeansComponent();
    }


    private void initializeRequiredBeansComponent() {

    }

    public <T> T getBean(Class<T> clazz) {
        return clazz.cast(beans.get(clazz));
    }

    public Object extractBeanInstance(Class<?> beanClass) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Object beanInstance = beans.get(beanClass);
        if (beanInstance == null) {

            beanInstance = beanClass.getDeclaredConstructor().newInstance();
            beans.put(beanClass, beanInstance);
            return beanInstance;
        }
        return beanInstance;
    }

    public void registerRoute(String basePackageName) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, IOException {
        List<Class<?>> classList = Utils.retrieveAllApplicationControllerClass(basePackageName);
        this.routeRegistry = Utils.urlMethodClassRegistry(classList);
    }

}
