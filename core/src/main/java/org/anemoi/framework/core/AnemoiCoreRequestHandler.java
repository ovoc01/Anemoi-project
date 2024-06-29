package org.anemoi.framework.core;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Builder;
import lombok.ToString;
import org.anemoi.framework.core.context.AnemoiContext;

import org.anemoi.framework.core.modelview.ModelView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;


public final class AnemoiCoreRequestHandler extends HttpServlet {
    AnemoiContext holder;
    private static final Logger logger = LoggerFactory.getLogger(AnemoiCoreRequestHandler.class);


    @Override
    public void init() throws ServletException {
        holder = (AnemoiContext) getServletContext().getAttribute("applicationContext");
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handleIncomingRequest(request, response);
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


    private void handleIncomingRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestInfo requestInfo = null;
        try {
            requestInfo = extractRequestMapping(request);
            logger.info("Request info {}", requestInfo);
            getClass().getDeclaredMethod(requestInfo.requestHandlerMethodName, RequestInfo.class, HttpServletRequest.class, HttpServletResponse.class).invoke(this, requestInfo, request, response);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            response.setContentType("application/json");
            response.getOutputStream().println(e.toString());
            logger.error("Error occurs and exception has been thrown ", e);
        }
    }

    private void handleModelViewRequest(RequestInfo requestInfo, HttpServletRequest request, HttpServletResponse response) throws InvocationTargetException, IllegalAccessException, ServletException, IOException, NoSuchMethodException {
        logger.info("MVC Request handler called");
        Object instance = requestInfo.declaringClass;
        ModelView modelView = (ModelView) requestInfo.method.invoke(instance);
        addAllAttributesToRequest(request,modelView.getData());
        request.getRequestDispatcher(modelView.getView()).forward(request, response);
    }

    private void handleRestAPIRequest(HttpServletRequest request, HttpServletResponse response) {
        //TODO
    }

    private void handleGraphQlRequest(HttpServletRequest request, HttpServletResponse response) {
        //TODO
    }

    private void handleInertiaJsRequest(HttpServletRequest request, HttpServletResponse response) {
        //TODO
    }


    private RequestInfo extractRequestMapping(HttpServletRequest request) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        return RequestInfo.init(request, this.holder);
    }

    private void addAllAttributesToRequest(HttpServletRequest request, Map<String,Object> attributes){
        attributes.forEach(request::setAttribute);
    }


    @Builder
    @ToString
    static
    class RequestInfo {
        String url;
        String httpMethod;
        Method method;
        Object declaringClass;
        String requestHandlerMethodName;

        public static RequestInfo init(HttpServletRequest request, AnemoiContext holder) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
            String url = request.getRequestURI();
            String httpMethod = request.getMethod();
            Method method = holder.getRouteRegistry().extractMethodFromRoute(httpMethod, url);
            Object declaringClassInstance = holder.extractBeanInstance(method.getDeclaringClass());
            return RequestInfo
                    .builder()
                    .url(url)
                    .httpMethod(httpMethod)
                    .method(method)
                    .declaringClass(declaringClassInstance)
                    .requestHandlerMethodName("handleModelViewRequest")
                    .build();
        }
    }
}
