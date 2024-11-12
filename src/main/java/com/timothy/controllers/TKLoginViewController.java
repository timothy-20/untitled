package com.timothy.controllers;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RequiredArgsConstructor
@Controller
@RequestMapping("/login")
public class TKLoginViewController {
    @GetMapping
    public RedirectView initialize(HttpSession session) {
        session.setMaxInactiveInterval(180);
        session.setAttribute("passwordLoginMessage", "");
        session.setAttribute("passwordLoginIdErrorMessage", "");
        session.setAttribute("passwordLoginPasswordErrorMessage", "");
        return new RedirectView("/login/user-account");
    }

    @GetMapping("/password")
    public String showPasswordLoginView(HttpSession session, Model model) {


        return "login/TKPasswordLoginView";
    }
}