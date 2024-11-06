package com.timothy;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.Duration;
import java.time.LocalDateTime;

public class TKAccessInterceptor implements HandlerInterceptor {
    private static final Logger ACCESS_LOGGER = LogManager.getLogger(TKAccessLog4jLogger.class);
    private final StringBuilder accessLogMessage;
    private LocalDateTime startDateTime;

    public TKAccessInterceptor() {
        super();
        this.accessLogMessage = new StringBuilder();
        this.startDateTime = null;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String servletPath = request.getServletPath();

        if (servletPath.startsWith("/")) {
            this.startDateTime = LocalDateTime.now();

            if (!this.accessLogMessage.isEmpty()) {
                this.accessLogMessage.setLength(0);
            }

            this.accessLogMessage.append(String.format("%s | %s | %s", request.getMethod(), request.getRemoteAddr(), servletPath));
        }

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);

        String servletPath = request.getServletPath();

        if (servletPath.startsWith("/")) {
            Duration duration = Duration.between(this.startDateTime, LocalDateTime.now());
            this.accessLogMessage.append(String.format(" | %d | %d ms | %d B | %d B", response.getStatus(), duration.toMillis(), request.getContentLengthLong(), response.getBufferSize()));
            ACCESS_LOGGER.info(this.accessLogMessage);
        }
    }
}
