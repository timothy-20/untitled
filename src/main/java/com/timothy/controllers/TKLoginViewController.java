package com.timothy.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/login")
public class TKLoginViewController {
    public TKLoginViewController() {
        super();
    }

    @GetMapping
    public RedirectView initialize() {
        return new RedirectView("/login/user-account");
    }

    @GetMapping("/user-account")
    public String showLoginView(Model model) {
        model.addAttribute("", "");
        return "/login/TKLoginView";
    }
}