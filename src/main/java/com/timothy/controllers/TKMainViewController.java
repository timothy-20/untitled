package com.timothy.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class TKMainViewController {
    public TKMainViewController() {
        super();
    }

    @GetMapping
    public String initialize() {
        return "TKMainView";
    }
}
