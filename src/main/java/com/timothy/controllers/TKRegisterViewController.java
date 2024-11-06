package com.timothy.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.hibernate.Session;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/register")
public class TKRegisterViewController {
    public TKRegisterViewController() {
        super();
    }

    @GetMapping
    public RedirectView initialize(HttpSession session) {
        throw new UnsupportedOperationException("Not supported yet.");

        // timeout 5분으로 설정
//        session.setMaxInactiveInterval(3);
//        session.setAttribute("isRegistrationStarted", Boolean.TRUE);
//        return new RedirectView("/register/first-step");
    }

    @GetMapping("/first-step")
    public String showRegisterFirstStepView(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "회원가입 세션이 유효하지 않습니다.");
        }

        Boolean isRegistrationStarted = (Boolean)session.getAttribute("isRegistrationStarted");

        if (isRegistrationStarted == null || !isRegistrationStarted) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "잘못된 접근입니다. 임의의 접근은 허용되지 않습니다.");
        }

        return "/register/TKRegisterFirstStepView";
    }

    @GetMapping("/second-step")
    public String showRegisterSecondStepView(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "회원가입 세션이 유효하지 않습니다.");
        }

        Boolean isRegistrationStarted = (Boolean)session.getAttribute("isRegistrationStarted");
        String realName = (String)session.getAttribute("realName");
        String nickname = (String)session.getAttribute("nickname");

        if ((isRegistrationStarted == null || !isRegistrationStarted)
        || realName == null
        || nickname == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "잘못된 접근입니다. 임의의 접근은 허용되지 않습니다.");
        }

        return "/register/TKRegisterSecondStepView";
    }

    @GetMapping("/third-step")
    public String showRegisterThirdStepView(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "회원가입 세션이 유효하지 않습니다.");
        }

        Boolean isRegistrationStarted = (Boolean)session.getAttribute("isRegistrationStarted");
        String birthday = (String)session.getAttribute("birthday");
        String gender = (String)session.getAttribute("gender");

        if ((isRegistrationStarted == null || !isRegistrationStarted)
        || birthday == null
        || gender == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "잘못된 접근입니다. 임의의 접근은 허용되지 않습니다.");
        }

        return "/register/TKRegisterThirdStepView";
    }

    @GetMapping("/finish")
    public String showRegisterFinishView(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "회원가입 세션이 유효하지 않습니다.");
        }

        Boolean isRegistrationStarted = (Boolean)session.getAttribute("isRegistrationStarted");
        String id = (String)session.getAttribute("id");
        String password = (String)session.getAttribute("password");

        if ((isRegistrationStarted == null || !isRegistrationStarted)
        || id == null
        || password == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "잘못된 접근입니다. 임의의 접근은 허용되지 않습니다.");
        }

        session.setAttribute("isRegistrationStarted", Boolean.FALSE);
        session.invalidate();
        return "/register/TKRegisterFinishView";
    }
}

