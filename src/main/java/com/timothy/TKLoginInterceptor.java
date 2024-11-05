package com.timothy;

import com.timothy.controllers.TKGlobalExceptionHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;

public class TKLoginInterceptor implements HandlerInterceptor {
    private static final Logger logger = LogManager.getLogger(TKGlobalExceptionHandler.class);

    public TKLoginInterceptor() {
        super();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String servletPath = request.getServletPath();
        String remoteAddr = request.getRemoteAddr();
        String localAddr = request.getLocalAddr();

        if (servletPath.startsWith("/login")) {
            HttpSession session = request.getSession();
        }

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
