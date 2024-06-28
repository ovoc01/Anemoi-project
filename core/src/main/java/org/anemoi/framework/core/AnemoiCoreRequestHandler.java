package org.anemoi.framework.core;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;
import org.anemoi.framework.core.context.AnemoiContextHolder;
import org.anemoi.framework.core.route.RouteRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;



public final class AnemoiCoreRequestHandler extends HttpServlet {
    private  AnemoiContextHolder holder;
    private static final Logger logger = LoggerFactory.getLogger(AnemoiCoreRequestHandler.class);


    @Override
    public void init() throws ServletException{
        holder = AnemoiContextHolder.getInstance();
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
