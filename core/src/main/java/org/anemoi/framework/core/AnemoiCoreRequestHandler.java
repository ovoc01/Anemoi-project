package org.anemoi.framework.core;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Builder;
import lombok.ToString;
import org.anemoi.framework.core.context.AnemoiContext;

import org.anemoi.framework.core.mapping.binding.ReqParam;
import org.anemoi.framework.core.mapping.binding.ToObject;
import org.anemoi.framework.core.modelview.ModelView;
import org.apache.commons.beanutils.ConvertUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import java.util.Arrays;
import java.util.Map;


public final class AnemoiCoreRequestHandler extends HttpServlet {
    AnemoiContext holder;
    private static final Logger logger = LoggerFactory.getLogger(AnemoiCoreRequestHandler.class);
    private static final String CONTROLLER_EXTENSION = ".ctl";


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
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException |
                 InvocationTargetException e) {
            response.setContentType("application/json");
            response.getOutputStream().println(e.toString());
            logger.error("Error occurs and exception has been thrown ", e);
        }
    }

    private void handleModelViewRequest(RequestInfo requestInfo, HttpServletRequest request, HttpServletResponse response) throws InvocationTargetException, IllegalAccessException, ServletException, IOException, NoSuchMethodException {
        logger.info("MVC Request handler called");
        Object instance = requestInfo.declaringClass;
        for (Object param :requestInfo.requestParametersValue){
            logger.info("param value {}",param.getClass());
        }
        ModelView modelView = requestInfo.requestParametersValue.length > 0 ?
                (ModelView) requestInfo.method.invoke(instance, requestInfo.requestParametersValue) : (ModelView) requestInfo.method.invoke(instance);
        addAllAttributesToRequest(request, modelView.getData());
        request.getRequestDispatcher(modelView.getView()).forward(request, response);
    }

    private Object[] extractRequestParameterValue(HttpServletRequest request, Parameter[] parameters) {
        return Arrays.stream(parameters)
                .map(param -> {
                    ReqParam reqParam = param.getAnnotation(ReqParam.class);
                    return request.getParameter(reqParam.value());
                })
                .toArray();
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

    private void addAllAttributesToRequest(HttpServletRequest request, Map<String, Object> attributes) {
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
        Object[] requestParametersValue;

        public static RequestInfo init(HttpServletRequest request, AnemoiContext holder) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

            String url = request.getRequestURI();
            String httpMethod = request.getMethod();
            Method method = holder.getRouteRegistry().extractMethodFromRoute(httpMethod, url);
            Object declaringClassInstance = holder.extractBeanInstance(method.getDeclaringClass());

            Object[] requestParameters = Arrays.stream(method.getParameters())
                    .map(parameter -> {
                        if (parameter.isAnnotationPresent(ReqParam.class)) {
                            ReqParam reqParam = parameter.getAnnotation(ReqParam.class);
                            String paramValue = request.getParameter(reqParam.value());
                            return paramValue != null ? ConvertUtils.convert(paramValue, parameter.getType()) : null;
                        } else if (parameter.isAnnotationPresent(ToObject.class)) {
                            try {
                                return requestParamToObject(request, parameter.getType());
                            } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                                     IllegalAccessException e) {
                                throw new RuntimeException(e);
                            }
                        } else {
                            return null;
                        }
                    }).toArray();

            return RequestInfo
                    .builder()
                    .url(url)
                    .httpMethod(httpMethod)
                    .method(method)
                    .declaringClass(declaringClassInstance)
                    .requestHandlerMethodName("handleModelViewRequest")
                    .requestParametersValue(requestParameters)
                    .build();
        }
    }


    private static Object requestParamToObject(HttpServletRequest request,Class<?> object) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Object obj = object.getDeclaredConstructor().newInstance();
        Arrays.stream(obj.getClass().getDeclaredFields()).map(field -> {
            field.setAccessible(true);
            try {
                field.set(obj,ConvertUtils.convert(request.getParameter(field.getName()),field.getType()));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            return null;
        });
        return obj;
    }



}
