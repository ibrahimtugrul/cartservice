package com.ibrahimtugrul.cartservice.infrastructure.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ClassUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 *
 */
@Slf4j
public class LogExecutionInterceptor extends HandlerInterceptorAdapter {

    private static final String REQUEST_START_TIME = "requestStartTime";

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final  Object handler) throws Exception {
        try {
            if (handler instanceof HandlerMethod) {
                final HandlerMethod handlerMethod = (HandlerMethod) handler;
                request.setAttribute(REQUEST_START_TIME, System.currentTimeMillis());
                log.info("Starting controller method for {}.{}",
                        ClassUtils.getShortClassName(handlerMethod.getBean().getClass()),
                        handlerMethod.getMethod().getName());
            }
        } catch (Exception e) {
            log.error("Caught an exception while executing handler method", e);
        }
        return true;
    }

    @Override
    public void afterCompletion(final HttpServletRequest request, final HttpServletResponse response, final Object handler, final Exception ex) throws Exception {
        try {
            if (handler instanceof HandlerMethod) {
                final HandlerMethod handlerMethod = (HandlerMethod) handler;
                log.info("Completed controller method for {}.{} takes {} ms",
                        ClassUtils.getShortClassName(handlerMethod.getBean().getClass()),
                        handlerMethod.getMethod().getName(),
                        System.currentTimeMillis() - (Long) request.getAttribute(REQUEST_START_TIME));
            }
        } catch (Exception e) {
            log.error("Caught an exception while executing handler method", e);
        }
    }
}