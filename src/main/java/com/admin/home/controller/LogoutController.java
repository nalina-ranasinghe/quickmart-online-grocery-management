package com.admin.home.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LogoutController {

    @GetMapping("/logout")
    public String logout() {
        // In a real application, this would invalidate the session
        return "redirect:/admin/login";
    }
} 