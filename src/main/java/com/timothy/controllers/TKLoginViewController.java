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
        return new RedirectView("/login/password");
    }

    @GetMapping("/password")
    public String showPasswordLoginView(HttpSession session, Model model) {
        if (session != null) {
            String idErrorMessage = (String)session.getAttribute("passwordLoginIdErrorMessage");

            if (idErrorMessage != null && !idErrorMessage.isEmpty()) {
                model.addAttribute("idErrorMessage", idErrorMessage);
                session.setAttribute("passwordLoginIdErrorMessage", "");
            }

            String passwordErrorMessage = (String)session.getAttribute("passwordLoginPasswordErrorMessage");

            if (passwordErrorMessage != null && !passwordErrorMessage.isEmpty()) {
                model.addAttribute("passwordErrorMessage", passwordErrorMessage);
                session.setAttribute("passwordLoginPasswordErrorMessage", "");
            }
        }

        return "login/TKPasswordLoginView";
    }
}