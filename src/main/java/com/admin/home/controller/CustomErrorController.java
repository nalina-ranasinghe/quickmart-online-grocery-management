package com.admin.home.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        
        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());
            
            if(statusCode == HttpStatus.NOT_FOUND.value()) {
                model.addAttribute("status", "404");
                model.addAttribute("message", "Page Not Found");
            } else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                model.addAttribute("status", "500");
                model.addAttribute("message", "Internal Server Error");
            } else if(statusCode == HttpStatus.FORBIDDEN.value()) {
                model.addAttribute("status", "403");
                model.addAttribute("message", "Access Denied");
            } else {
                model.addAttribute("status", statusCode);
                model.addAttribute("message", "An error occurred");
            }
        }
        
        return "error";
    }
} 