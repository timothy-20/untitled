package com.timothy;

import com.timothy.entities.TKUserEntity;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.HandlerInterceptor;

public class TKRegistrationInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        HttpSession session = request.getSession(false);

        if (!requestURI.equals("/register") && requestURI.startsWith("/register")) {
            // 회원가입 세션 생성 요청을 제외한 회원가입 요청인 경우
            if (session == null) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "회원가입 세션이 유효하지 않습니다.");
            }

            Boolean isRegistrationStarted = (Boolean)session.getAttribute("isRegistrationStarted");
            TKUserEntity user = (TKUserEntity)session.getAttribute("registrationUserEntity");

            if ((isRegistrationStarted == null || !isRegistrationStarted) || user == null) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "잘못된 접근입니다. 임의의 접근은 허용되지 않습니다.");
            }
        }

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
