package org.anemoi.framework.core;

import org.anemoi.framework.core.context.AnemoiContextHolder;
import org.anemoi.framework.core.utils.Utils;
import org.anemoi.framework.server.AnemoiJettyServerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnemoiFrameworkApplication {
    private static final Logger logger = LoggerFactory.getLogger(AnemoiFrameworkApplication.class);
    public static void launch(Class<?> mainApplicationClass) {
        logger.info("Starting anemoi framework ");
        String basePackage = mainApplicationClass.getPackageName();

        AnemoiJettyServerImpl server = new AnemoiJettyServerImpl(9090);
        AnemoiContextHolder contextHolder = AnemoiContextHolder.getInstance();
        AnemoiCoreRequestHandler coreRequestHandler = null;
        try {
            contextHolder.registerRoute(basePackage);
            coreRequestHandler =  new AnemoiCoreRequestHandler(contextHolder);
            server.launch(coreRequestHandler.getClass());
        } catch (Exception e) {
           logger.error("Exception has been thrown",e);
        }


    }
}
