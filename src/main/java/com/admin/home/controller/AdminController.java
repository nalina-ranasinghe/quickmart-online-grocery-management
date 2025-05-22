package com.admin.home.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import jakarta.servlet.http.HttpSession;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private static final String ADMIN_CREDENTIALS_FILE = "admin_credentials.txt";

    @GetMapping("/login")
    public String showLoginPage(HttpSession session) {
        // If already logged in, redirect to home
        if (session.getAttribute("adminLoggedIn") != null) {
            return "redirect:/admin/home";
        }
        return "Admin-Login";
    }

    @PostMapping("/login")
    public ModelAndView processLogin(@RequestParam String username, 
                                   @RequestParam String password,
                                   HttpSession session) {
        // Validate admin credentials from txt file
        if (validateAdminCredentials(username, password)) {
            // Set session attributes
            session.setAttribute("adminLoggedIn", true);
            session.setAttribute("adminUsername", username);
            return new ModelAndView("redirect:/admin/home");
        } else {
            ModelAndView modelAndView = new ModelAndView("Admin-Login");
            modelAndView.addObject("error", "Invalid credentials. Please try again.");
            return modelAndView;
        }
    }

    @GetMapping("/home")
    public String showOverview(HttpSession session) {
        // Check if user is logged in
        if (session.getAttribute("adminLoggedIn") == null) {
            return "redirect:/admin/login";
        }
        return "admin-dashboard";
    }

    @GetMapping("/user-management")
    public String showUserManagement(HttpSession session) {
        if (session.getAttribute("adminLoggedIn") == null) {
            return "redirect:/admin/login";
        }
        return "user-management";
    }

    @GetMapping("/order-overview")
    public String showOrderOverview(HttpSession session) {
        if (session.getAttribute("adminLoggedIn") == null) {
            return "redirect:/admin/login";
        }
        return "order-management";
    }

    // Helper method to validate admin credentials from text file
    private boolean validateAdminCredentials(String username, String password) {
        try {
            Resource resource = new ClassPathResource(ADMIN_CREDENTIALS_FILE);
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(":");
                    if (parts.length == 2 && parts[0].equals(username) && parts[1].equals(password)) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
} 