package com.nl.payout.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
    @GetMapping("/product")
    public String indexPage() {
        return "index";
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }
}
