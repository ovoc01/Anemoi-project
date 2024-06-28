package org.anemoi.framework.core;

import org.anemoi.framework.core.context.AnemoiContextHolder;
import org.anemoi.framework.core.utils.Utils;
import org.anemoi.framework.server.AnemoiJettyServerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnemoiFrameworkApplication {
    private static final Logger logger = LoggerFactory.getLogger(AnemoiFrameworkApplication.class);
    static String basePackage = null;
    public static void launch(Class<?> mainApplicationClass) {
        logger.info("Starting anemoi framework ");
        basePackage = mainApplicationClass.getPackageName();

        AnemoiJettyServerImpl server = new AnemoiJettyServerImpl(9090);

        try {

            server.launch(AnemoiCoreRequestHandler.class);
        } catch (Exception e) {
           logger.error("Exception has been thrown",e);
        }


    }
}
