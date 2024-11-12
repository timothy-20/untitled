package com.timothy.controllers;

import com.timothy.entities.TKUserEntity;
import com.timothy.models.TKRealName;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.view.RedirectView;

import java.time.LocalDate;

@RequiredArgsConstructor
@Controller
@RequestMapping("/register")
public class TKRegisterViewController {
    @GetMapping
    public RedirectView initialize(HttpSession session) {
        // timeout 5분으로 설정
        session.setMaxInactiveInterval(300);
        session.setAttribute("isRegistrationStarted", Boolean.TRUE);
        session.setAttribute("registrationUserEntity", new TKUserEntity());
        return new RedirectView("/register/first-step");
    }

    @GetMapping("/first-step")
    public String showRegisterFirstStepView(HttpSession session, Model model) {
        TKUserEntity user = (TKUserEntity)session.getAttribute("registrationUserEntity");
        String realName = user.getRealName();

        if (realName != null) {
            int separateIndex = realName.indexOf(',');

            if (separateIndex >= 0) {
                model.addAttribute("lastName", realName.substring(0, separateIndex));
                model.addAttribute("firstName", realName.substring(separateIndex + 2));
            } else {
                model.addAttribute("firstName", realName);
            }

            if (user.getNickname() != null) {
                model.addAttribute("nickname", user.getNickname());
            }
        }

        return "/register/TKRegisterFirstStepView";
    }

    @GetMapping("/second-step")
    public String showRegisterSecondStepView(HttpSession session, Model model) {
        TKUserEntity user = (TKUserEntity)session.getAttribute("registrationUserEntity");

        if (user.getRealName() == null || user.getRealName().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "잘못된 접근입니다, 이전 단계를 건너뛰었을 가능성이 있습니다. 임의의 접근은 허용되지 않습니다.");
        }

        model.addAttribute("birthday", user.getBirthday() != null ? user.getBirthday().toString() : null);
        model.addAttribute("gender", user.getGender() != null ? user.getGender() : "none");
        return "/register/TKRegisterSecondStepView";
    }

    @GetMapping("/third-step")
    public String showRegisterThirdStepView(HttpSession session) {
        TKUserEntity user = (TKUserEntity) session.getAttribute("registrationUserEntity");

        if (user.getBirthday() == null || user.getGender() == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "잘못된 접근입니다, 이전 단계를 건너뛰었을 가능성이 있습니다. 임의의 접근은 허용되지 않습니다.");
        }

        return "/register/TKRegisterThirdStepView";
    }

    @GetMapping("/finish")
    public String showRegisterFinishView(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        TKUserEntity user = (TKUserEntity) session.getAttribute("registrationUserEntity");

        if (user.getId() == null || user.getPassword() == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "잘못된 접근입니다, 이전 단계를 건너뛰었을 가능성이 있습니다. 임의의 접근은 허용되지 않습니다.");
        }

        session.removeAttribute("isRegistrationStarted");
        session.removeAttribute("registrationUserEntity");
        session.invalidate();
        return "/register/TKRegisterFinishView";
    }
}

