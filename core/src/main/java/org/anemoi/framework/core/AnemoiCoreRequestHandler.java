package org.anemoi.framework.core;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Builder;
import lombok.ToString;
import org.anemoi.framework.core.context.AnemoiContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public final class AnemoiCoreRequestHandler extends HttpServlet {
    AnemoiContext holder;
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


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handleIncomingRequest(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.getOutputStream().println("<h1>Hello world from Post Method</h1>");
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.getOutputStream().println("<h1>Hello world Put Method</h1>");
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.getOutputStream().println("<h1>Hello world from Delete Method</h1>");
    }


    private void handleIncomingRequest(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
        RequestInfo requestInfo = extractRequestMapping(request);
        logger.info("Request info url:{} method:{}",requestInfo.url,requestInfo.httpMethod);

    }

    private RequestInfo extractRequestMapping(HttpServletRequest request) {
        return RequestInfo.init(request,this.holder);
    }



    @Builder
    @ToString
    class RequestInfo{
        String url;
        String httpMethod;
        Method method;
        Object declaringClass;


        public static RequestInfo init(HttpServletRequest request,AnemoiContext holder){
            return RequestInfo
                    .builder()
                    .url(request.getRequestURI())
                    .httpMethod(request.getMethod())
                    .method(holder.getBean(null))
                    .build();
        }
    }
}
