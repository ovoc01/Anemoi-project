package org.anemoi.framework;


import org.anemoi.framework.core.mapping.binding.GetMapping;
import org.anemoi.framework.core.utils.Utils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Map<String, Method> getMethodRoute = Utils.getSpecifiedHttpRequestTypeRoute(Test.class, GetMapping.class);
        System.out.println(getMethodRoute);
    }
}