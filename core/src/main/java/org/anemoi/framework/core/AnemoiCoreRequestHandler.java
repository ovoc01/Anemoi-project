package org.anemoi.framework.core;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.anemoi.framework.core.context.AnemoiContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;



public final class AnemoiCoreRequestHandler extends HttpServlet {
    private AnemoiContext holder;
    private static final Logger logger = LoggerFactory.getLogger(AnemoiCoreRequestHandler.class);


    @Override
    public void init() throws ServletException{
        holder = (AnemoiContext) getServletContext().getAttribute("applicationContext");
        try {
            holder.registerRoute(AnemoiFrameworkApplication.basePackage);
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException | IOException e) {
            logger.error("Exception has been thrown",e);
        }
    }

     public AnemoiCoreRequestHandler(){

     }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.getOutputStream().println("<h1>Hello world</h1>");
    }
}
