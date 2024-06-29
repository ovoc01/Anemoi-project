package org.anemoi.framework.server.listener;


import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.anemoi.framework.core.AnemoiFrameworkApplication;
import org.anemoi.framework.core.context.AnemoiContext;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.beanutils.converters.DateTimeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;


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
            loadDefaultConverterPattern();
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

    private void loadDefaultConverterPattern(){
        DateConverter dateConverter = new DateConverter();
        dateConverter.setPattern("yyyy-MM-dd");

        ConvertUtils.register(dateConverter, java.util.Date.class);
        ConvertUtils.register(dateConverter, LocalDate.class);
        ConvertUtils.register(dateConverter, java.sql.Date.class);

        DateTimeConverter dateTimeConverter = new DateConverter();

        dateTimeConverter.setPattern("yyyy-MM-dd'T'HH:mm:ss");
        ConvertUtils.register(dateTimeConverter, Timestamp.class);
        ConvertUtils.register(dateTimeConverter, LocalDateTime.class);
    }
}
