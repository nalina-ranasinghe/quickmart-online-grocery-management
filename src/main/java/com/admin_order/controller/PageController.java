package com.admin_order.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class PageController {

    @GetMapping({"/home", "/dashboard"})
    public String adminHome() {
        return "redirect:/admin/order-overview";
    }

    @GetMapping("/order-overview")
    public String orderManagement() {
        return "order-management";
    }

    @GetMapping("/user-management")
    public String userManagement() {
        return "user-management";
    }

    @GetMapping("/prime-members")
    public String primeMembers() {
        return "prime-members";
    }

    @GetMapping("/product-management")
    public String productManagement() {
        return "products";
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/logout";
    }
}