package com.quickmart.User.controller;

import com.quickmart.User.model.User;
import com.quickmart.User.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import java.io.File;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String home() {
        return "login";
    }

    @GetMapping("/login")
    public String loginPage(HttpSession session) {
        if (session.getAttribute("user") != null) {
            return "redirect:/store/products"; // Updated path
        }
        return "login";
    }

    @GetMapping("/register")
    public String registerPage(HttpSession session) {
        if (session.getAttribute("user") != null) {
            return "redirect:/store/products"; // Updated path
        }
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String username,
                               @RequestParam String password,
                               @RequestParam String email,
                               Model model) {
        System.out.println("=== Registration Attempt ===");
        System.out.println("Username: [" + username + "]");
        System.out.println("Password length: " + password.length());
        System.out.println("Email: [" + email + "]");

        userService.listAllUsers();

        User user = new User(username, password, email, "");
        if (userService.registerUser(user)) {
            System.out.println("Registration successful for: " + username);
            return "redirect:/login?registered";
        } else {
            System.out.println("Registration failed for: " + username);
            model.addAttribute("error", "Username already exists");
            return "register";
        }
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam String username,
                            @RequestParam String password,
                            Model model,
                            HttpSession session) {
        System.out.println("=== Login Attempt ===");
        System.out.println("Username/Email: [" + username + "]");
        System.out.println("Password length: " + password.length());

        File userFile = new File("users.txt");
        if (!userFile.exists()) {
            System.out.println("Error: users.txt file does not exist");
            model.addAttribute("error", "System error: User database not found");
            return "login";
        }

        userService.listAllUsers();

        User user = userService.authenticateUser(username, password);
        if (user != null) {
            System.out.println("Login successful for: " + username);
            session.setAttribute("user", user);
            return "redirect:/store/products"; // Updated path
        } else {
            System.out.println("Login failed for: " + username);
            model.addAttribute("error", "Invalid username or password");
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login?logout";
    }

    @GetMapping("/store/products") // Updated path to be more specific
    public String products(HttpSession session) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }
        return "products";
    }
}