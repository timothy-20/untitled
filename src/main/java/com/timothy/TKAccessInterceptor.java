package com.timothy;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.time.Duration;
import java.time.LocalDateTime;

public class TKAccessInterceptor implements HandlerInterceptor {
    private static final Logger LOGGER = LogManager.getLogger(TKAccessLog4jLogger.class);
    private LocalDateTime startDateTime;

    public TKAccessInterceptor() {
        super();
        this.startDateTime = null;
    }

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
        this.startDateTime = LocalDateTime.now();
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);

        String servletPath = request.getServletPath();

        if (servletPath.startsWith("/")) {
            Duration duration = Duration.between(this.startDateTime, LocalDateTime.now());
            LOGGER.info(String.format("%s [%s] %s:%d | %s | %d ms | %d B",
                    request.getRemoteAddr(),
                    request.getMethod(),
                    request.getRequestURL(),
                    request.getServerPort(),
                    response.getStatus(),
                    duration.toMillis(),
                    response.getBufferSize()));
        }
    }
}
