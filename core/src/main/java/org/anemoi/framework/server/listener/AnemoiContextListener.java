package org.anemoi.framework.server.listener;


import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.anemoi.framework.core.AnemoiFrameworkApplication;
import org.anemoi.framework.core.context.AnemoiContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

@WebListener("/*")
public class AnemoiContextListener implements ServletContextListener {
    private static final Logger logger = LoggerFactory.getLogger(AnemoiContextListener.class);
    private static AnemoiContext context;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        context = new AnemoiContext();
        try {
            context.registerRoute(AnemoiFrameworkApplication.basePackage);
            sce.getServletContext().setAttribute("applicationContext",context);
            logger.info("Application starting up");
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException | IOException e) {
            logger.error("Exception has been thrown",e);
        }

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        sce.getServletContext().removeAttribute("applicationContext");
        logger.info("Application is shutting down");
    }

    public static AnemoiContext getApplicationContext() {
        return context;
    }
}
