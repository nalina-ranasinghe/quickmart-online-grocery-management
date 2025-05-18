package com.admin_dashboard.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

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
    public String showLoginPage() {
        return "Admin-Login";
    }

    @PostMapping("/home")
    public ModelAndView processLogin(@RequestParam String username, @RequestParam String password) {
        // Validate admin credentials from txt file
        if (validateAdminCredentials(username, password)) {
            return new ModelAndView("redirect:/admin/home");
        } else {
            ModelAndView modelAndView = new ModelAndView("Admin-Login");
            modelAndView.addObject("error", "Invalid credentials. Please try again.");
            return modelAndView;
        }
    }

    @GetMapping("/home")
    public String showOverview() {
        return "admin-dashboard";
    }

    @GetMapping("/user-management")
    public String showUserManagement() {
        return "user-management";
    }

    @GetMapping("/prime-members")
    public String showPrimeMembers() {
        return "prime-members";
    }

    @GetMapping("/product-management")
    public String showProductManagement() {
        return "product-management";
    }

    @GetMapping("/order-overview")
    public String showOrderOverview() {
        return "order-overview";
    }

    // Helper method to validate admin credentials from text file
    private boolean validateAdminCredentials(String username, String password) {
        try {
            Resource resource = new ClassPathResource(ADMIN_CREDENTIALS_FILE);
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8));
            String line;
            
            // Expected format in file: username:password
            while ((line = reader.readLine()) != null) {
                String[] credentials = line.split(":");
                if (credentials.length == 2) {
                    String storedUsername = credentials[0].trim();
                    String storedPassword = credentials[1].trim();
                    
                    if (storedUsername.equals(username) && storedPassword.equals(password)) {
                        reader.close();
                        return true;
                    }
                }
            }
            reader.close();
        } catch (IOException e) {
            // File not found or cannot be read, fallback to default admin/admin123
            if ("admin".equals(username) && "admin123".equals(password)) {
                return true;
            }
        }
        return false;
    }
} 