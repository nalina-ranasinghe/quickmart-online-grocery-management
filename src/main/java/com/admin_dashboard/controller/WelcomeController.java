package com.admin_dashboard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WelcomeController {

    @GetMapping("/")
    public String welcome() {
        return "redirect:/admin/login";
    }
    
    @GetMapping("/admin")
    public String adminHome() {
        return "redirect:/admin/login";
    }
} 