package com.admin_order.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
    @GetMapping({"/order-management", "/"})
    public String orderManagement() {
        return "order-management";
    }
} 