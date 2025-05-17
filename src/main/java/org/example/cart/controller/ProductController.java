package org.example.cart.controller;

import org.example.cart.model.Product;
import org.example.cart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private CartService cartService;

    @GetMapping
    public String listProducts(Model model) {
        model.addAttribute("products", cartService.getAllProducts());
        model.addAttribute("cartItems", cartService.getAllCartItems());
        return "products";
    }
} 