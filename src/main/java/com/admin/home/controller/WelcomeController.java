package com.admin.home.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpSession;

@Controller
public class WelcomeController {

    @GetMapping("/")
    public String welcome(HttpSession session) {
        // If already logged in, redirect to home
        if (session.getAttribute("adminLoggedIn") != null) {
            return "redirect:/admin/home";
        }
        return "redirect:/admin/login";
    }
    
    @GetMapping("/admin")
    public String adminHome() {
        // Always show login page for /admin
        return "redirect:/admin/login";
    }
}