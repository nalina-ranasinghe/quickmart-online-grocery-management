package com.admin.common.exception;

import com.admin.product.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(
            ResourceNotFoundException ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", ex.getMessage());
        body.put("status", HttpStatus.NOT_FOUND.value());
        
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ModelAndView handleNoHandlerFoundException(NoHandlerFoundException ex) {
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("status", "404");
        modelAndView.addObject("message", "Page Not Found");
        return modelAndView;
    }
    
    @ExceptionHandler(Exception.class)
    public Object handleGlobalException(Exception ex, WebRequest request) {
        // Check if the request is for an API endpoint
        String path = request.getDescription(false);
        if (path.contains("/api/")) {
            // Handle as REST API error
            Map<String, Object> body = new HashMap<>();
            body.put("timestamp", LocalDateTime.now());
            body.put("message", "An unexpected error occurred");
            body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            // Handle as view error
            ModelAndView modelAndView = new ModelAndView("error");
            modelAndView.addObject("status", "500");
            modelAndView.addObject("message", "Internal Server Error");
            return modelAndView;
        }
    }
} 