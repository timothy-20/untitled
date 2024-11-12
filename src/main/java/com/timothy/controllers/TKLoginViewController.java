package com.timothy.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RequiredArgsConstructor
@Controller
@RequestMapping("/login")
public class TKLoginViewController {
    @GetMapping
    public RedirectView initialize() {
        return new RedirectView("/login/user-account");
    }

    @GetMapping("/user-account")
    public String showLoginView() {
        return "/login/TKLoginView";
    }
}